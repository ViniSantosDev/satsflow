-- Schema para H2 Database (desenvolvimento)

-- Tabela de Usuários
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT RANDOM_UUID(),
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    wallet_address VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- Tabela de Ordens
CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY DEFAULT RANDOM_UUID(),
    buyer_id UUID NOT NULL,
    seller_id UUID,
    amount_brl DECIMAL(18,2) NOT NULL,
    amount_btc DECIMAL(18,8) NOT NULL,
    price_at_creation DECIMAL(18,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    matched_at TIMESTAMP,
    FOREIGN KEY (buyer_id) REFERENCES users(id) ON DELETE NO ACTION,
    FOREIGN KEY (seller_id) REFERENCES users(id) ON DELETE NO ACTION
);

-- Tabela de Transações de Escrow
CREATE TABLE IF NOT EXISTS escrow_transactions (
    id UUID PRIMARY KEY DEFAULT RANDOM_UUID(),
    order_id UUID NOT NULL UNIQUE,
    buyer_id UUID NOT NULL,
    seller_id UUID NOT NULL,
    amount_btc DECIMAL(18,8) NOT NULL,
    amount_brl DECIMAL(18,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'HELD',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    released_at TIMESTAMP,
    refunded_at TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (buyer_id) REFERENCES users(id) ON DELETE NO ACTION,
    FOREIGN KEY (seller_id) REFERENCES users(id) ON DELETE NO ACTION
);

-- Tabela de Avaliações de Usuários
CREATE TABLE IF NOT EXISTS user_ratings (
    id UUID PRIMARY KEY DEFAULT RANDOM_UUID(),
    rater_id UUID NOT NULL,
    rated_user_id UUID NOT NULL,
    order_id UUID,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (rater_id) REFERENCES users(id) ON DELETE NO ACTION,
    FOREIGN KEY (rated_user_id) REFERENCES users(id) ON DELETE NO ACTION,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE SET NULL
);

-- Tabela de Mensagens de Chat
CREATE TABLE IF NOT EXISTS chat_messages (
    id UUID PRIMARY KEY DEFAULT RANDOM_UUID(),
    order_id UUID NOT NULL,
    sender_id UUID NOT NULL,
    content TEXT NOT NULL,
    sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE NO ACTION
);

-- Tabela de Auditoria/Logs
CREATE TABLE IF NOT EXISTS audit_logs (
    id UUID PRIMARY KEY DEFAULT RANDOM_UUID(),
    user_id UUID,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50),
    entity_id UUID,
    description TEXT,
    ip_address VARCHAR(45),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_orders_buyer_id ON orders(buyer_id);
CREATE INDEX IF NOT EXISTS idx_orders_seller_id ON orders(seller_id);
CREATE INDEX IF NOT EXISTS idx_orders_created_at ON orders(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_escrow_order_id ON escrow_transactions(order_id);
CREATE INDEX IF NOT EXISTS idx_escrow_status ON escrow_transactions(status);
CREATE INDEX IF NOT EXISTS idx_ratings_rated_user ON user_ratings(rated_user_id);
CREATE INDEX IF NOT EXISTS idx_ratings_order ON user_ratings(order_id);
CREATE INDEX IF NOT EXISTS idx_chat_order_id ON chat_messages(order_id);
CREATE INDEX IF NOT EXISTS idx_chat_sender_id ON chat_messages(sender_id);
CREATE INDEX IF NOT EXISTS idx_chat_sent_at ON chat_messages(sent_at);
CREATE INDEX IF NOT EXISTS idx_audit_user_id ON audit_logs(user_id);
CREATE INDEX IF NOT EXISTS idx_audit_created_at ON audit_logs(created_at DESC);

