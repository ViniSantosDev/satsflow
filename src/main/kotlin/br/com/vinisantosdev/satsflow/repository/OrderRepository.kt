package br.com.vinisantosdev.satsflow.repository

import br.com.vinisantosdev.satsflow.domain.Order
import br.com.vinisantosdev.satsflow.domain.Trade
import br.com.vinisantosdev.satsflow.domain.User
import br.com.vinisantosdev.satsflow.enuns.OrderStatus
import br.com.vinisantosdev.satsflow.enuns.TradeStatus
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.util.UUID

interface OrderRepository : ReactiveCrudRepository<Order, UUID> {

    fun findByStatus(status: OrderStatus): Flux<Order>

    fun save(order: Order): Mono<Order>

    fun findByUserIdAndStatus(userId: UUID, status: OrderStatus): Flux<Order>

    // Busca ordens de venda compatíveis com uma ordem de compra
    // (tipo SELL, abertas, preço <= o que o comprador quer pagar)
    @Query("""
        SELECT * FROM orders
        WHERE type = 'SELL'
          AND status = 'OPEN'
          AND price_per_btc <= :maxPrice
          AND amount_btc >= :minAmount
        ORDER BY price_per_btc ASC, created_at ASC
        LIMIT :limit
    """)
    fun findMatchingSellOrders(
        maxPrice: BigDecimal,
        minAmount: BigDecimal,
        limit: Int = 10,
    ): Flux<Order>

    // Busca ordens de compra compatíveis com uma ordem de venda
    @Query("""
        SELECT * FROM orders
        WHERE type = 'BUY'
          AND status = 'OPEN'
          AND price_per_btc >= :minPrice
          AND amount_btc >= :minAmount
        ORDER BY price_per_btc DESC, created_at ASC
        LIMIT :limit
    """)
    fun findMatchingBuyOrders(
        minPrice: BigDecimal,
        minAmount: BigDecimal,
        limit: Int = 10,
    ): Flux<Order>
}


