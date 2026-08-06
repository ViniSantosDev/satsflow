package br.com.vinisantosdev.satsflow.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table("chat_messages")
data class ChatMessage(
    @Id
    val id: UUID? = null,

    @Column("trade_id")
    val tradeId: UUID, // mensagens sempre atreladas a um trade específico

    @Column("sender_id")
    val senderId: UUID,

    @Column("receiver_id")
    val receiverId: UUID,

    val content: String,

    @Column("is_read")
    val isRead: Boolean = false,

    @Column("created_at")
    val createdAt: Instant = Instant.now(),
    val orderId: UUID,
)