package br.com.vinisantosdev.satsflow.dto

import br.com.vinisantosdev.satsflow.domain.Order
import br.com.vinisantosdev.satsflow.enuns.OrderStatus
import br.com.vinisantosdev.satsflow.enuns.OrderType
import br.com.vinisantosdev.satsflow.enuns.PaymentMethod
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.util.UUID

data class CreateOrderRequest(
    val amountBrl: BigDecimal,
    val type: OrderType,
    val paymentMethod: PaymentMethod,
)

fun CreateOrderRequest.toDomain(userId: UUID, pricePerBtc: BigDecimal): Order {
    val now = Instant.now()
    val amountBtc = this.amountBrl.divide(pricePerBtc, 8, RoundingMode.HALF_UP)

    return Order(
        id = UUID.randomUUID(),
        userId = userId,
        type = this.type,
        amountBtc = amountBtc,
        pricePerBtc = pricePerBtc,
        status = OrderStatus.OPEN,
        createdAt = now,
        updatedAt = now,
        totalBrl = this.amountBrl,
        paymentMethod = this.paymentMethod,
        expiresAt = null,
    )
}