package br.com.vinisantosdev.satsflow.domain

import br.com.vinisantosdev.satsflow.enuns.OrderStatus
import br.com.vinisantosdev.satsflow.enuns.OrderType
import br.com.vinisantosdev.satsflow.enuns.PaymentMethod
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

// ── Order ─────────────────────────────────────────────────────────────────────
// Representa uma ordem de compra ou venda de Bitcoin no livro de ordens P2P

@Table("orders")
data class Order(
    @Id val id: UUID = UUID.randomUUID(),
    val userId: UUID,
    val type: OrderType,
    val status: OrderStatus = OrderStatus.OPEN,

    /** Quantidade de Bitcoin (ex: 0.001 BTC) */
    val amountBtc: BigDecimal,

    /** Preço em BRL por 1 BTC (ex: 350000.00) */
    val pricePerBtc: BigDecimal,

    /** Valor total = amountBtc * pricePerBtc */
    val totalBrl: BigDecimal = amountBtc * pricePerBtc,

    val paymentMethod: PaymentMethod,
    val expiresAt: Instant? = null,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
)