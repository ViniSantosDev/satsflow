package br.com.vinisantosdev.satsflow.usecase

import br.com.vinisantosdev.satsflow.domain.ChatMessage
import br.com.vinisantosdev.satsflow.domain.Trade
import br.com.vinisantosdev.satsflow.repository.ChatMessageRepository
import br.com.vinisantosdev.satsflow.repository.TradeRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.UUID

@Service
class ChatUseCase(
    private val chatMessageRepository: ChatMessageRepository,
    private val tradeRepository: TradeRepository,
) {

    fun sendMessage(orderId: UUID, senderId: UUID, content: String): Mono<ChatMessage> =
        resolveTrade(orderId, senderId)
            .flatMap { trade ->
                val receiverId = if (trade.buyerId == senderId) trade.sellerId else trade.buyerId
                chatMessageRepository.save(
                    ChatMessage(
                        id = null,
                        orderId = orderId,
                        tradeId = trade.id,
                        senderId = senderId,
                        receiverId = receiverId,
                        content = content,
                        isRead = false,
                        createdAt = Instant.now(),
                    )
                )
            }

    fun listMessages(orderId: UUID, requesterId: UUID): Flux<ChatMessage> =
        resolveTrade(orderId, requesterId)
            .thenMany(chatMessageRepository.findByOrderId(orderId))

    // Busca o trade da ordem e já valida se o remetente/requisitante é parte dele
    private fun resolveTrade(orderId: UUID, userId: UUID): Mono<Trade> =
        tradeRepository.findByBuyOrderIdOrSellOrderId(orderId, orderId)
            .switchIfEmpty(Mono.error(IllegalStateException("Chat só é liberado após o match da ordem")))
            .flatMap { trade ->
                if (trade.buyerId == userId || trade.sellerId == userId)
                    Mono.just(trade)
                else
                    Mono.error(SecurityException("Usuário não autorizado a acessar esse chat"))
            }
}