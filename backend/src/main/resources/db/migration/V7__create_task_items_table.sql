-- 依頼明細テーブル（依頼と品目の中間テーブル）
CREATE TABLE task_items (
    id SERIAL PRIMARY KEY,
    task_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(id)
);
