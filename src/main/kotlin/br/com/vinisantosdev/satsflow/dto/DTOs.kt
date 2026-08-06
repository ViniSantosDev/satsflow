package br.com.vinisantosdev.satsflow.dto

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID


// DTOs para requisições

data class AcceptOrderRequest(
    val sellerId: UUID? = null, // Será preenchido do token
    val buyerId: UUID? = null
)

data class ReleaseEscrowRequest(
    val orderId: UUID,
    val reason: String? = null
)

data class RefundEscrowRequest(
    val orderId: UUID,
    val reason: String
)

data class CreateRatingRequest(
    val ratedUserId: UUID,
    val orderId: UUID,
    val rating: Int,
    val comment: String? = null
)

data class ChatMessageRequest(
    val senderId: UUID? = null, // Será preenchido do token
    val content: String
)

// respostas DTOs
data class OrderDTO(
    val id: UUID,
    val buyerId: UUID,
    val sellerId: UUID?,
    val amountBrl: BigDecimal,
    val amountBtc: BigDecimal,
    val priceAtCreation: BigDecimal,
    val status: String,
    val escrowStatus: String?,
    val createdAt: Instant,
    val matchedAt: Instant?,
    val completedAt: Instant?,
    val cancelledAt: Instant?
)

data class TransactionSummary(
    val totalVolumeBrl: BigDecimal,
    val totalVolumeBtc: BigDecimal,
    val completedTransactions: Int,
    val averageCompletionTime: String
)

// Paginação
data class PagedResponse<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)

