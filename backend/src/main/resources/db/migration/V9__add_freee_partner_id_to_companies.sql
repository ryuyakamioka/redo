-- companiesテーブルにfreee_partner_idカラムを追加
ALTER TABLE companies ADD COLUMN freee_partner_id BIGINT;

-- freee_partner_idにインデックスを追加（検索性能向上のため）
CREATE INDEX idx_companies_freee_partner_id ON companies(freee_partner_id);
