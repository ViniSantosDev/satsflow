package br.com.vinisantosdev.satsflow.infra

import br.com.vinisantosdev.satsflow.usecase.PriceQuoteService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.math.BigDecimal

// TODO: trocar por integração real (exchange/oráculo de preço) antes de ir pra produção
@Service
class MockPriceQuoteService : PriceQuoteService {
    override fun getCurrentBtcPrice(): Mono<BigDecimal> =
        Mono.just(BigDecimal("350000.00")) // valor fixo temporário
}