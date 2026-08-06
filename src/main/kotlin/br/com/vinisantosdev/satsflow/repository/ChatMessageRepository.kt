package br.com.vinisantosdev.satsflow.repository

import br.com.vinisantosdev.satsflow.domain.ChatMessage
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.UUID

@Repository
interface ChatMessageRepository : R2dbcRepository<ChatMessage, UUID> {
    
    @Query("SELECT * FROM chat_messages WHERE order_id = :orderId ORDER BY sent_at ASC")
    fun findByOrderId(orderId: UUID): Flux<ChatMessage>
    
    @Query("SELECT * FROM chat_messages WHERE order_id = :orderId AND sender_id = :senderId ORDER BY sent_at DESC LIMIT 1")
    fun findLastMessageFromSender(orderId: UUID, senderId: UUID): Flux<ChatMessage>
    
    @Query("SELECT COUNT(*) FROM chat_messages WHERE order_id = :orderId")
    fun countByOrderId(orderId: UUID): Flux<Long>
}