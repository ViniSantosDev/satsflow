package br.com.vinisantosdev.satsflow.repository

import br.com.vinisantosdev.satsflow.domain.EscrowTransaction
import br.com.vinisantosdev.satsflow.enuns.EscrowStatus
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
interface EscrowTransactionRepository : R2dbcRepository<EscrowTransaction, UUID> {
    
    @Query("SELECT * FROM escrow_transactions WHERE order_id = :orderId")
    fun findByOrderId(orderId: UUID): Mono<EscrowTransaction>
    
    @Query("SELECT * FROM escrow_transactions WHERE buyer_id = :buyerId ORDER BY created_at DESC")
    fun findByBuyerId(buyerId: UUID): Flux<EscrowTransaction>
    
    @Query("SELECT * FROM escrow_transactions WHERE seller_id = :sellerId ORDER BY created_at DESC")
    fun findBySellerId(sellerId: UUID): Flux<EscrowTransaction>
    
    @Query("SELECT * FROM escrow_transactions WHERE status = :status ORDER BY created_at DESC")
    fun findByStatus(status: EscrowStatus): Flux<EscrowTransaction>
}

