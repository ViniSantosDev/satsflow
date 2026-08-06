package br.com.vinisantosdev.satsflow.enuns

enum class EscrowStatus {
    PENDING,     // aguardando lock do BTC do vendedor
    LOCKED,      // BTC travado, aguardando confirmação de pagamento em BRL
    RELEASED,    // pagamento confirmado, BTC liberado pro comprador
    REFUNDED,    // disputa resolvida a favor do vendedor, BTC devolvido
    DISPUTED,    // em disputa, aguardando resolução manual
}