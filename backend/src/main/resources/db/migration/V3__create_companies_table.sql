CREATE TABLE companies (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    withholding_tax BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- サンプルデータを挿入
INSERT INTO companies (name, withholding_tax) VALUES
('株式会社サンプル', TRUE),
('テスト株式会社', FALSE),
('ABC商事', TRUE);
