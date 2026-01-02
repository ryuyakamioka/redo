-- 依頼ステータスマスタテーブル
CREATE TABLE task_statuses (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    display_color VARCHAR(50) NOT NULL,
    display_order INTEGER NOT NULL,
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 既存のenum値と互換性を保つ初期データ
INSERT INTO task_statuses (code, name, display_color, display_order, is_completed) VALUES
('TODO', 'TODO', 'gray', 1, false),
('IN_PROGRESS', '作業中', 'blue', 2, false),
('DONE', '完了', 'green', 3, true);
