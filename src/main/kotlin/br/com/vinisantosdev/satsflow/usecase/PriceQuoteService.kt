package br.com.vinisantosdev.satsflow.usecase

import reactor.core.publisher.Mono
import java.math.BigDecimal

interface PriceQuoteService {
    fun getCurrentBtcPrice(): Mono<BigDecimal>
}