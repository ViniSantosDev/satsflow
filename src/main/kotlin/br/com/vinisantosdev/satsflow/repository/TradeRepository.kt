package br.com.vinisantosdev.satsflow.repository

import br.com.vinisantosdev.satsflow.domain.Trade
import br.com.vinisantosdev.satsflow.enuns.TradeStatus
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

interface TradeRepository : ReactiveCrudRepository<Trade, UUID> {
    fun findByBuyerIdOrSellerId(buyerId: UUID, sellerId: UUID): Flux<Trade>
    fun findByStatus(status: TradeStatus): Flux<Trade>
    fun findByBuyOrderIdOrSellOrderId(orderId: UUID, orderId2: UUID): Mono<Trade>
}