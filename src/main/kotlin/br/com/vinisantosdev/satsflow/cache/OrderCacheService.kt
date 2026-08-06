package br.com.vinisantosdev.satsflow.cache

import br.com.vinisantosdev.satsflow.domain.Order
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

@Service
class OrderCacheService {

    // Cache simples em memória — trocar por Redis quando houver mais de uma instância rodando
    private val cache = AtomicReference<List<Order>?>(null)
    private val rateLimitMap = ConcurrentHashMap<String, MutableList<Instant>>()


    fun getOpenOrders(): Flux<Order> =
        cache.get()?.let { Flux.fromIterable(it) } ?: Flux.empty()

    fun cacheOpenOrders(orders: List<Order>): Mono<Void> =
        Mono.fromRunnable { cache.set(orders) }

    fun evictOpenOrders(): Mono<Void> =
        Mono.fromRunnable { cache.set(null) }

    // Rate limit simples em memória — ex: máx 5 ordens por usuário a cada 10s
    fun checkRateLimit(userId: String): Mono<Boolean> = Mono.fromCallable {
        val now = Instant.now()
        val windowStart = now.minusSeconds(10)

        val timestamps = rateLimitMap.computeIfAbsent(userId) { mutableListOf() }
        synchronized(timestamps) {
            timestamps.removeIf { it.isBefore(windowStart) }
            if (timestamps.size >= 5) {
                false
            } else {
                timestamps.add(now)
                true
            }
        }
    }
}