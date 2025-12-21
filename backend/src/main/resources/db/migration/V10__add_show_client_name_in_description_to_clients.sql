-- 依頼人テーブルに摘要表示フラグを追加
ALTER TABLE clients ADD COLUMN show_client_name_in_description BOOLEAN NOT NULL DEFAULT TRUE;

-- 既存データのデフォルト値を設定（依頼人名を表示する）
UPDATE clients SET show_client_name_in_description = TRUE WHERE show_client_name_in_description IS NULL;
