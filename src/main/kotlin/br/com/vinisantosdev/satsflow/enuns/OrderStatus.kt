package br.com.vinisantosdev.satsflow.enuns

enum class OrderStatus {
    OPEN,       // Visível no livro de ordens
    MATCHED,    // Encontrou contraparte, aguardando pagamento
    COMPLETED,  // Trade concluído
    CANCELLED,  // Cancelada pelo usuário ou expirou
    EXPIRED
}