package br.com.vinisantosdev.satsflow.repository

import br.com.vinisantosdev.satsflow.infra.UserRating
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.UUID

@Repository
interface UserRatingRepository : R2dbcRepository<UserRating, UUID> {
    
    @Query("SELECT * FROM user_ratings WHERE rated_user_id = :userId ORDER BY created_at DESC")
    fun findByRatedUserId(userId: UUID): Flux<UserRating>
    
    @Query("SELECT * FROM user_ratings WHERE rater_id = :userId ORDER BY created_at DESC")
    fun findByRaterId(userId: UUID): Flux<UserRating>
    
    @Query("SELECT AVG(rating) FROM user_ratings WHERE rated_user_id = :userId")
    fun getAverageRating(userId: UUID): Flux<Double>
    
    @Query("SELECT COUNT(*) FROM user_ratings WHERE rated_user_id = :userId")
    fun countRatingsForUser(userId: UUID): Flux<Long>
}

