package br.com.vinisantosdev.satsflow.dto


import br.com.vinisantosdev.satsflow.domain.Order
import br.com.vinisantosdev.satsflow.enuns.OrderStatus
import br.com.vinisantosdev.satsflow.enuns.OrderType
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class OrderResponse(
    val id: UUID,
    val userId: UUID,
    val type: OrderType,
    val amountBtc: BigDecimal,
    val pricePerBtc: BigDecimal,
    val status: OrderStatus,
    val createdAt: Instant,
)

fun Order.toResponse(): OrderResponse = OrderResponse(
    id = this.id,
    userId = this.userId,
    type = this.type,
    amountBtc = this.amountBtc,
    pricePerBtc = this.pricePerBtc,
    status = this.status,
    createdAt = this.createdAt,
)