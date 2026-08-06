package br.com.vinisantosdev.satsflow.dto

data class AuthResponse(
    val token: String,
    val user: UserDTO
)