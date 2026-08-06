package br.com.vinisantosdev.satsflow.dto

import java.util.UUID

data class UserDTO(
    val id: UUID,
    val email: String,
    val username: String,
    val reputationScore: Double,
    val totalTrades: Int,
    val completedTrades: Int,
    val suspended: Boolean
)