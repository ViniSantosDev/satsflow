package br.com.vinisantosdev.satsflow.usecase

import br.com.vinisantosdev.satsflow.domain.Order
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.math.BigDecimal



@Component
class OrderValidationChain {

    private val rules: List<(Order) -> String?> = listOf(
        { o -> if (o.amountBtc <= BigDecimal.ZERO) "Quantidade de BTC deve ser maior que zero" else null },
        { o -> if (o.pricePerBtc <= BigDecimal.ZERO) "Preço por BTC deve ser maior que zero" else null },
    )

    fun  build(): OrderValidator = OrderValidator { order ->
        val error = rules.firstNotNullOfOrNull { it(order) }
        if (error != null) Mono.error(IllegalArgumentException(error)) else Mono.just(order)
    }
}