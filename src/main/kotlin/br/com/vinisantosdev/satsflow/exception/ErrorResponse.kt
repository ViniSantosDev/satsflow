package br.com.vinisantosdev.satsflow.exception

import java.time.Instant

data class ErrorResponse(
    val message: String,
    val timestamp: Instant = Instant.now(),
    val status: Int = 400
)