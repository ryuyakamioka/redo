# Redo - 依頼管理システム

依頼（タスク）の管理を行うWebアプリケーションです。依頼人、会社、品目などのマスタデータを管理し、依頼の登録・削除が可能です。

## 技術スタック

### バックエンド
- **Java**: 21 LTS
- **Spring Boot**: 3.4.1
- **Spring Data JDBC**: データベースアクセス
- **PostgreSQL**: 15.12
- **Flyway**: データベースマイグレーション
- **Maven**: ビルドツール

### フロントエンド
- **Nuxt 3**: 3.15.4
- **Vue 3**: Composition API
- **TypeScript**: 型安全性
- **Pinia**: 状態管理
- **Axios**: HTTP通信
- **Tailwind CSS**: スタイリング（CDN版）
- **pnpm**: パッケージマネージャ

### インフラ
- **AWS EC2**: アプリケーションサーバー
- **GitHub Actions**: CI/CD
- **PM2**: Nuxtアプリケーションのプロセス管理
- **systemd**: Spring Bootアプリケーションのサービス管理

## 機能

### 依頼管理
- 依頼の登録（依頼名、ステータス、依頼日、納品予定日など）
- 依頼の一覧表示
- 依頼の削除

### マスタデータ管理
- **会社**: 会社名、源泉徴収税の有無
- **依頼人**: 依頼人名、略称、所属会社
- **品目**: 品目名、単価

### 依頼明細
- 1つの依頼に複数の品目を登録可能
- 品目選択時に単価×件数で金額を自動計算
- 金額は手動で編集も可能

## プロジェクト構成

モノレポを採用しています。

## セットアップ

### 前提条件

- Java 21以上
- Node.js 22以上
- pnpm 9以上
- PostgreSQL 15以上

### データベースのセットアップ

```bash
# PostgreSQLにログイン
psql -U postgres

# データベースとユーザーを作成
CREATE DATABASE redo_db;
CREATE USER redo_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE redo_db TO redo_user;
```

### バックエンドのセットアップ

```bash
cd backend

# application.propertiesを編集してデータベース接続情報を設定
# src/main/resources/application.properties

# アプリケーションの起動（Flywayマイグレーションが自動実行されます）
./mvnw spring-boot:run
```

バックエンドは `http://localhost:8080/api` で起動します。

### フロントエンドのセットアップ

```bash
cd frontend

# 依存関係のインストール
pnpm install

# 開発サーバーの起動
pnpm dev
```

フロントエンドは `http://localhost:3000` で起動します。

## API仕様

Swaggerを参照してください。

## テスト

### バックエンドのテスト実行

```bash
cd backend
./mvnw test
```

### テストの種類
- **単体テスト**: UseCase層のビジネスロジックテスト
- **統合テスト**: Repository層のデータアクセステスト
- **APIテスト**: Controller層のエンドポイントテスト（MockMvc使用）

## デプロイ

GitHub Actionsを使用した自動デプロイが設定されています。

## ライセンス

このプロジェクトはプライベートプロジェクトです。
