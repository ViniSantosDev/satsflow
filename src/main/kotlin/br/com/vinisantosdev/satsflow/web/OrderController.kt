package br.com.vinisantosdev.satsflow.web

import br.com.vinisantosdev.satsflow.dto.CreateOrderRequest
import br.com.vinisantosdev.satsflow.dto.OrderResponse
import br.com.vinisantosdev.satsflow.cache.OrderCacheService
import br.com.vinisantosdev.satsflow.usecase.OrderUseCase
import com.nimbusds.jose.jwk.source.RateLimitReachedException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(
    private val orderUseCase: OrderUseCase,
    private val cacheService: OrderCacheService,
) {

    // POST /api/v1/orders — cria uma nova ordem
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(
        @Validated @RequestBody request: CreateOrderRequest,
        @AuthenticationPrincipal jwt: Jwt,
    ): Mono<OrderResponse> {
        val userId = UUID.fromString(jwt.subject)

        // Rate limiting via Redis antes de processar
        return cacheService.checkRateLimit(userId.toString())
            .flatMap { allowed ->
                if (!allowed)
                    Mono.error(RateLimitReachedException())
                else
                    orderUseCase.createOrder(userId, request)
            }
    }

    // GET /api/v1/orders — lista ordens abertas (com cache Redis)
    @GetMapping
    fun listOpenOrders(): Flux<OrderResponse> = orderUseCase.listOpenOrders()

    // GET /api/v1/orders/mine — ordens do usuário autenticado
    @GetMapping("/mine")
    fun myOrders(@AuthenticationPrincipal jwt: Jwt): Flux<OrderResponse> {
        val userId = UUID.fromString(jwt.subject)
        return orderUseCase.getUserOrders(userId)
    }

    // DELETE /api/v1/orders/{id} — cancela uma ordem
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun cancelOrder(
        @PathVariable id: UUID,
        @AuthenticationPrincipal jwt: Jwt,
    ): Mono<Void> {
        val userId = UUID.fromString(jwt.subject)
        return orderUseCase.cancelOrder(id, userId)
    }

    // GET /api/v1/orders/stream — SSE: feed em tempo real de novas ordens
    // Clientes conectam aqui e recebem eventos toda vez que uma nova ordem é criada
    @GetMapping("/stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun orderStream(): Flux<OrderResponse> =
        orderUseCase.listOpenOrders()
            .repeatWhen { it.delayElements(java.time.Duration.ofSeconds(5)) }
}
