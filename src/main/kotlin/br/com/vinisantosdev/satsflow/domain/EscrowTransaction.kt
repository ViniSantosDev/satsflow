package br.com.vinisantosdev.satsflow.domain

import br.com.vinisantosdev.satsflow.enuns.EscrowStatus
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Table("escrow_transactions")
data class EscrowTransaction(
    @Id
    val id: UUID? = null,

    @Column("order_id")
    val orderId: UUID,

    @Column("buyer_id")
    val buyerId: UUID,

    @Column("seller_id")
    val sellerId: UUID,

    @Column("amount_btc")
    val amountBtc: BigDecimal,

    val status: EscrowStatus = EscrowStatus.PENDING,

    @Column("created_at")
    val createdAt: Instant = Instant.now(),

    @Column("updated_at")
    val updatedAt: Instant = Instant.now(),

    @Column("released_at")
    val releasedAt: Instant? = null,
)