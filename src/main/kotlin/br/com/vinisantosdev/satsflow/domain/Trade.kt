package br.com.vinisantosdev.satsflow.domain

import br.com.vinisantosdev.satsflow.enuns.TradeStatus
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

// ── Trade ─────────────────────────────────────────────────────────────────────
// Criado quando uma ordem BUY faz match com uma SELL

@Table("trades")
data class Trade(
    @Id val id: UUID = UUID.randomUUID(),
    val buyOrderId: UUID,
    val sellOrderId: UUID,
    val buyerId: UUID,
    val sellerId: UUID,
    val amountBtc: BigDecimal,
    val pricePerBtc: BigDecimal,
    val totalBrl: BigDecimal,
    val status: TradeStatus = TradeStatus.PENDING_PAYMENT,
    val createdAt: Instant = Instant.now(),
    val completedAt: Instant? = null,
)