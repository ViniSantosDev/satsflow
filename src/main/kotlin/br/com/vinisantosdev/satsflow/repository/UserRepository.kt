package br.com.vinisantosdev.satsflow.repository

import br.com.vinisantosdev.satsflow.domain.User
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
interface UserRepository : R2dbcRepository<User, UUID> {

    @Query("SELECT * FROM users WHERE email = :email")
    fun findByEmail(email: String): Mono<User>

    @Query("SELECT * FROM users WHERE username = :username")
    fun findByUsername(username: String): Mono<User>

    @Query("SELECT * FROM users WHERE suspended = false ORDER BY reputation_score DESC LIMIT :limit")
    fun findTopUsersByReputation(limit: Int): Mono<List<User>>
}

