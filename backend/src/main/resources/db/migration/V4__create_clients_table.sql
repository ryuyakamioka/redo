CREATE TABLE clients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    abbreviation VARCHAR(50) NOT NULL,
    company_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (company_id) REFERENCES companies(id)
);

-- サンプルデータを挿入
INSERT INTO clients (name, abbreviation, company_id) VALUES
('顧客A', 'A', 1),
('顧客B', 'B', 1),
('顧客C', 'C', 2),
('顧客D', 'D', 3);
