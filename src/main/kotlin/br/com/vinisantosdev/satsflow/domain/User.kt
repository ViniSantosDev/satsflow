package br.com.vinisantosdev.satsflow.domain

import br.com.vinisantosdev.satsflow.enuns.KycStatus
import br.com.vinisantosdev.satsflow.enuns.UserRole
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table("users")
data class User(
    @Id val id: UUID = UUID.randomUUID(),
    val email: String,
    val passwordHash: String,
    val username: String,
    val role: UserRole = UserRole.USER,
    val kycStatus: KycStatus = KycStatus.PENDING,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
)
