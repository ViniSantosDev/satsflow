package br.com.vinisantosdev.satsflow.usecase

import br.com.vinisantosdev.satsflow.domain.Order
import br.com.vinisantosdev.satsflow.domain.Trade
import reactor.core.publisher.Mono

interface OrderEventPublisher {
    fun publishOrderCreated(order: Order): Mono<Void>
    fun publishOrderMatched(trade: Trade): Mono<Void>
}