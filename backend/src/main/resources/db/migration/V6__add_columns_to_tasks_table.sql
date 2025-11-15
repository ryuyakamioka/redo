-- tasksテーブルに新しいカラムを追加
ALTER TABLE tasks
ADD COLUMN request_date DATE,
ADD COLUMN client_id BIGINT,
ADD COLUMN note TEXT,
ADD COLUMN expected_delivery_date DATE,
ADD COLUMN delivery_date DATE,
ADD CONSTRAINT fk_tasks_client FOREIGN KEY (client_id) REFERENCES clients(id);

-- 既存データ用のコメント: 既存のレコードは新しいカラムがNULLになります
