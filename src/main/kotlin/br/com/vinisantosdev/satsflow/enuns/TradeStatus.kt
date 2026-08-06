package br.com.vinisantosdev.satsflow.enuns

enum class TradeStatus {
    PENDING_PAYMENT,  // Comprador precisa pagar
    PAYMENT_CONFIRMED, // Vendedor confirma recebimento
    COMPLETED,        // Bitcoin liberado ao comprador
    DISPUTED,         // Em disputa
    CANCELLED
}