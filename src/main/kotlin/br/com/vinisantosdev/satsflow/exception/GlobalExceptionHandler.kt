package br.com.vinisantosdev.satsflow.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.NoSuchElementException


@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(
        ex: IllegalStateException,
        exchange: ServerWebExchange
    ): Mono<ResponseEntity<ErrorResponse>> {
        val errorResponse = ErrorResponse(
            message = ex.message ?: "Operação inválida",
            status = HttpStatus.BAD_REQUEST.value()
        )
        return Mono.just(ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        ex: IllegalArgumentException,
        exchange: ServerWebExchange
    ): Mono<ResponseEntity<ErrorResponse>> {
        val errorResponse = ErrorResponse(
            message = ex.message ?: "Argumento inválido",
            status = HttpStatus.BAD_REQUEST.value()
        )
        return Mono.just(ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST))
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(
        ex: NoSuchElementException,
        exchange: ServerWebExchange
    ): Mono<ResponseEntity<ErrorResponse>> {
        val errorResponse = ErrorResponse(
            message = "Recurso não encontrado",
            status = HttpStatus.NOT_FOUND.value()
        )
        return Mono.just(ResponseEntity(errorResponse, HttpStatus.NOT_FOUND))
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(
        ex: Exception,
        exchange: ServerWebExchange
    ): Mono<ResponseEntity<ErrorResponse>> {
        val errorResponse = ErrorResponse(
            message = "Erro interno do servidor",
            status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        )
        ex.printStackTrace()
        return Mono.just(ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR))
    }
}