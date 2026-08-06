-- Tabela de Ordens (compatível com H2 em memória)
CREATE TABLE orders (
    id UUID PRIMARY KEY,
    buyer_id UUID NOT NULL,
    seller_id UUID,
    amount_brl DECIMAL(18,2) NOT NULL,
    amount_btc DECIMAL(18,8) NOT NULL,
    price_at_creation DECIMAL(18,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    matched_at TIMESTAMP
);

-- Tabela de Mensagens de Chat
CREATE TABLE chat_messages (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    sender_id UUID NOT NULL,
    content TEXT NOT NULL,
    sent_at TIMESTAMP NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

-- Índices para performance
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_buyer_id ON orders(buyer_id);
CREATE INDEX idx_orders_seller_id ON orders(seller_id);
CREATE INDEX idx_chat_order_id ON chat_messages(order_id);
CREATE INDEX idx_chat_sender_id ON chat_messages(sender_id);
