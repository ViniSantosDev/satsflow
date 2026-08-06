package br.com.vinisantosdev.satsflow.messaging

import br.com.vinisantosdev.satsflow.domain.Order
import br.com.vinisantosdev.satsflow.domain.Trade
import br.com.vinisantosdev.satsflow.usecase.OrderEventPublisher
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class SpringOrderEventPublisher(
    private val publisher: ApplicationEventPublisher,
) : OrderEventPublisher {

    override fun publishOrderCreated(order: Order): Mono<Void> =
        Mono.fromRunnable { publisher.publishEvent(OrderCreatedEvent(order)) }


    override fun publishOrderMatched(trade: Trade): Mono<Void> =
        Mono.fromRunnable { publisher.publishEvent(OrderMatchedEvent(trade)) }
}

data class OrderCreatedEvent(val order: Order)
data class OrderMatchedEvent(val trade: Trade)