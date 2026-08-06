package br.com.vinisantosdev.satsflow.usecase

import br.com.vinisantosdev.satsflow.domain.Order
import reactor.core.publisher.Mono

fun interface OrderValidator {
    fun validate(order: Order): Mono<Order>
}