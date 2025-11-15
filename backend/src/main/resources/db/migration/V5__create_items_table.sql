CREATE TABLE items (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- サンプルデータを挿入
INSERT INTO items (name, unit_price) VALUES
('データ入力', 500.00),
('書類作成', 1000.00),
('校正作業', 800.00),
('翻訳作業', 1500.00);
