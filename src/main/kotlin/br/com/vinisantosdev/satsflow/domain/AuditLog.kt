package br.com.vinisantosdev.satsflow.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table("audit_logs")
data class AuditLog(
    @Id
    val id: UUID? = null,

    @Column("user_id")
    val userId: UUID,

    val action: String, // ex: "ORDER_CREATED", "ORDER_CANCELLED", "TRADE_MATCHED"

    @Column("entity_id")
    val entityId: UUID, // id da ordem/trade/entidade afetada

    @Column("entity_type")
    val entityType: String, // ex: "ORDER", "TRADE", "USER"

    val details: String? = null, // snapshot em JSON do que mudou (opcional)

    @Column("ip_address")
    val ipAddress: String? = null,

    @Column("created_at")
    val createdAt: Instant = Instant.now(),
)