package br.com.vinisantosdev.satsflow.dto

data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String
)
