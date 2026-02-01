# ---------- build stage ----------
FROM node:20-alpine AS build

WORKDIR /app

# pnpm を有効化
RUN corepack enable

COPY package.json pnpm-lock.yaml ./
RUN pnpm install --frozen-lockfile

COPY . .
RUN pnpm build

# ---------- runtime stage ----------
FROM node:20-alpine

WORKDIR /app

RUN corepack enable

COPY --from=build /app/.output ./.output
COPY --from=build /app/package.json ./

EXPOSE 3000

CMD ["node", ".output/server/index.mjs"]
