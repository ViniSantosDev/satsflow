package br.com.vinisantosdev.satsflow.usecase

import br.com.vinisantosdev.satsflow.cache.OrderCacheService
import br.com.vinisantosdev.satsflow.dto.CreateOrderRequest
import br.com.vinisantosdev.satsflow.domain.Order
import br.com.vinisantosdev.satsflow.dto.OrderResponse
import br.com.vinisantosdev.satsflow.domain.Trade
import br.com.vinisantosdev.satsflow.dto.toDomain
import br.com.vinisantosdev.satsflow.dto.toResponse
import br.com.vinisantosdev.satsflow.enuns.OrderStatus
import br.com.vinisantosdev.satsflow.enuns.OrderType
import br.com.vinisantosdev.satsflow.repository.OrderRepository
import br.com.vinisantosdev.satsflow.repository.TradeRepository
import br.com.vinisantosdev.satsflow.strategy.OrderMatchingContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class OrderUseCase(
    private val orderRepository: OrderRepository,
    private val tradeRepository: TradeRepository,
    private val matchingContext: OrderMatchingContext,
    private val validationChain: OrderValidationChain,
    private val cacheService: OrderCacheService, // <-- estava tipado como OrderValidationChain
    private val eventPublisher: OrderEventPublisher,
    private val priceQuoteService: PriceQuoteService,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun createOrder(userId: UUID, request: CreateOrderRequest): Mono<OrderResponse> {
        return priceQuoteService.getCurrentBtcPrice()
            .map { price -> request.toDomain(userId, price) }
            .flatMap { order -> validationChain.build().validate(order) }
            .flatMap { validOrder -> orderRepository.save(validOrder) }
            .flatMap { savedOrder ->
                eventPublisher.publishOrderCreated(savedOrder)
                    .thenReturn(savedOrder)
            }
            .flatMap { savedOrder ->
                tryMatch(savedOrder).thenReturn(savedOrder)
            }
            .map { it.toResponse() }
            .doOnSuccess { log.info("Ordem criada: ${it.id} tipo=${it.type} userId=$userId") }
    }

    fun listOpenOrders(): Flux<OrderResponse> =
        cacheService.getOpenOrders()
            .switchIfEmpty(
                orderRepository.findByStatus(OrderStatus.OPEN)
                    .collectList()
                    .flatMapMany { orders ->
                        cacheService.cacheOpenOrders(orders)
                            .thenMany(Flux.fromIterable(orders))
                    }
            )
            .map { it.toResponse() }

    fun getUserOrders(userId: UUID): Flux<OrderResponse> =
        orderRepository.findByUserIdAndStatus(userId, OrderStatus.OPEN)
            .map { it.toResponse() }

    @Transactional
    fun cancelOrder(orderId: UUID, userId: UUID): Mono<Void> =
        orderRepository.findById(orderId)
            .filter { it.userId == userId }
            .switchIfEmpty(Mono.error(IllegalArgumentException("Ordem não encontrada ou sem permissão")))
            .filter { it.status == OrderStatus.OPEN }
            .switchIfEmpty(Mono.error(IllegalStateException("Apenas ordens abertas podem ser canceladas")))
            .map { it.copy(status = OrderStatus.CANCELLED) }
            .flatMap { orderRepository.save(it) }
            .flatMap { cacheService.evictOpenOrders() }
            .then()

    private fun tryMatch(order: Order): Mono<Void> =
        matchingContext.findMatches(order)
            .filter { it.userId != order.userId }
            .next()
            .flatMap { matchedOrder ->
                val (buyOrder, sellOrder) = if (order.type == OrderType.BUY)
                    order to matchedOrder
                else
                    matchedOrder to order

                val trade = Trade(
                    buyOrderId = buyOrder.id,
                    sellOrderId = sellOrder.id,
                    buyerId = buyOrder.userId,
                    sellerId = sellOrder.userId,
                    amountBtc = minOf(buyOrder.amountBtc, sellOrder.amountBtc),
                    pricePerBtc = sellOrder.pricePerBtc,
                    totalBrl = minOf(buyOrder.amountBtc, sellOrder.amountBtc) * sellOrder.pricePerBtc,
                )

                tradeRepository.save(trade)
                    .flatMap { savedTrade ->
                        val updateBuy = orderRepository.save(buyOrder.copy(status = OrderStatus.MATCHED))
                        val updateSell = orderRepository.save(sellOrder.copy(status = OrderStatus.MATCHED))

                        Mono.zip(updateBuy, updateSell)
                            .flatMap {
                                cacheService.evictOpenOrders()
                                    .then(eventPublisher.publishOrderMatched(savedTrade))
                            }
                            .doOnSuccess {
                                log.info("Match realizado! Trade=${savedTrade.id} buy=${buyOrder.id} sell=${sellOrder.id}")
                            }
                    }
            }
            .then()
}