package br.com.vinisantosdev.satsflow.strategy

import br.com.vinisantosdev.satsflow.domain.Order
import br.com.vinisantosdev.satsflow.enuns.OrderType
import br.com.vinisantosdev.satsflow.repository.OrderRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

// ── Strategy Pattern ──────────────────────────────────────────────────────────
// Define o contrato para diferentes algoritmos de matching de ordens.
// Isso aplica o princípio O do SOLID: aberto para extensão, fechado para modificação.
// Para adicionar um novo algoritmo, basta criar uma nova implementação — sem tocar no código existente.

interface OrderMatchingStrategy {
    fun findMatches(order: Order): Flux<Order>
}

// Estratégia 1: Best Price — casa com o melhor preço disponível
@Component("bestPriceStrategy")
class BestPriceMatchingStrategy(
    private val orderRepository: OrderRepository,
) : OrderMatchingStrategy {

    override fun findMatches(order: Order): Flux<Order> = when (order.type) {
        OrderType.BUY -> orderRepository.findMatchingSellOrders(
            maxPrice = order.pricePerBtc,
            minAmount = order.amountBtc,
        )
        OrderType.SELL -> orderRepository.findMatchingBuyOrders(
            minPrice = order.pricePerBtc,
            minAmount = order.amountBtc,
        )
    }
}

// Estratégia 2: Exact Amount — só casa se a quantidade for exata
@Component("exactAmountStrategy")
class ExactAmountMatchingStrategy(
    private val orderRepository: OrderRepository,
) : OrderMatchingStrategy {

    override fun findMatches(order: Order): Flux<Order> = when (order.type) {
        OrderType.BUY -> orderRepository.findMatchingSellOrders(
            maxPrice = order.pricePerBtc,
            minAmount = order.amountBtc,
        ).filter { it.amountBtc == order.amountBtc }

        OrderType.SELL -> orderRepository.findMatchingBuyOrders(
            minPrice = order.pricePerBtc,
            minAmount = order.amountBtc,
        ).filter { it.amountBtc == order.amountBtc }
    }
}

// ── Context — usa a estratégia injetada ───────────────────────────────────────
// Aplica o D do SOLID: depende de abstrações, não de implementações concretas.

@Component
class OrderMatchingContext(
    private val bestPriceStrategy: BestPriceMatchingStrategy,
) {
    // Por padrão usa BestPrice, mas pode ser trocado em runtime
    private var strategy: OrderMatchingStrategy = bestPriceStrategy

    fun setStrategy(strategy: OrderMatchingStrategy) {
        this.strategy = strategy
    }

    fun findMatches(order: Order): Flux<Order> = strategy.findMatches(order)
}
