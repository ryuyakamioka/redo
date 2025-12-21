-- 依頼テーブルに請求日を追加
ALTER TABLE tasks ADD COLUMN billing_date DATE;

-- インデックスを追加（請求日でのフィルタリングが多いため）
CREATE INDEX idx_tasks_billing_date ON tasks(billing_date);
