-- freee OAuth トークン管理テーブル
CREATE TABLE freee_tokens (
    id BIGSERIAL PRIMARY KEY,
    access_token TEXT NOT NULL,
    refresh_token TEXT NOT NULL,
    token_type VARCHAR(50) NOT NULL DEFAULT 'Bearer',
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 通常は1レコードのみ存在
CREATE UNIQUE INDEX idx_freee_tokens_single_row ON freee_tokens ((1));
