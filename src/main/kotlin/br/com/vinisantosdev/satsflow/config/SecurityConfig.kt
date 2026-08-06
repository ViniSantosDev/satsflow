package br.com.vinisantosdev.satsflow.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.web.server.SecurityWebFilterChain
import javax.crypto.spec.SecretKeySpec

@Configuration
class SecurityConfig(
    @Value("\${jwt.secret}") private val jwtSecret: String,
) {

    @Bean
    fun securityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http
            .csrf { it.disable() }
            .authorizeExchange { auth ->
                auth
                    // Endpoints públicos
                    .pathMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                    .pathMatchers(HttpMethod.GET, "/api/v1/orders").permitAll()
                    .pathMatchers(HttpMethod.GET, "/api/v1/orders/stream").permitAll()
                    .pathMatchers("/actuator/health", "/actuator/info").permitAll()
                    // Tudo mais requer autenticação
                    .anyExchange().authenticated()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.jwtDecoder(jwtDecoder())
                }
            }
            .build()

    @Bean
    fun jwtDecoder(): ReactiveJwtDecoder {
        val key = SecretKeySpec(jwtSecret.toByteArray(), "HmacSHA256")
        return NimbusReactiveJwtDecoder.withSecretKey(key).build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}

