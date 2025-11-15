<template>
  <aside class="w-64 bg-white border-r border-gray-200 flex flex-col">
    <!-- ロゴエリア -->
    <div class="p-6 border-b border-gray-200">
      <h1 class="text-xl font-bold text-gray-800">Redo</h1>
      <p class="text-xs text-gray-500 mt-1">依頼管理システム</p>
    </div>

    <!-- ナビゲーションメニュー -->
    <nav class="flex-1 p-4 overflow-y-auto">
      <div class="space-y-1">
        <!-- 依頼管理セクション -->
        <div class="mb-4">
          <p class="px-3 text-xs font-semibold text-gray-400 uppercase tracking-wider mb-2">
            依頼管理
          </p>
          <NuxtLink
            to="/"
            class="flex items-center px-3 py-2 text-sm font-medium rounded-lg transition-colors"
            :class="isActive('/') ? 'bg-blue-50 text-blue-700' : 'text-gray-700 hover:bg-gray-50'"
          >
            <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
            </svg>
            依頼一覧
          </NuxtLink>
        </div>

        <!-- マスタ管理セクション -->
        <div class="mb-4">
          <p class="px-3 text-xs font-semibold text-gray-400 uppercase tracking-wider mb-2">
            マスタ管理
          </p>
          <NuxtLink
            to="/companies"
            class="flex items-center px-3 py-2 text-sm font-medium rounded-lg transition-colors"
            :class="isActive('/companies') ? 'bg-blue-50 text-blue-700' : 'text-gray-700 hover:bg-gray-50'"
          >
            <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
            </svg>
            会社
          </NuxtLink>
          <NuxtLink
            to="/clients"
            class="flex items-center px-3 py-2 text-sm font-medium rounded-lg transition-colors"
            :class="isActive('/clients') ? 'bg-blue-50 text-blue-700' : 'text-gray-700 hover:bg-gray-50'"
          >
            <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
            </svg>
            依頼人
          </NuxtLink>
          <NuxtLink
            to="/items"
            class="flex items-center px-3 py-2 text-sm font-medium rounded-lg transition-colors"
            :class="isActive('/items') ? 'bg-blue-50 text-blue-700' : 'text-gray-700 hover:bg-gray-50'"
          >
            <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" />
            </svg>
            品目
          </NuxtLink>
        </div>
      </div>
    </nav>

    <!-- フッター -->
    <div class="p-4 border-t border-gray-200">
      <p class="text-xs text-gray-500 text-center">© 2025 Redo</p>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router';

const route = useRoute();

const isActive = (path: string) => {
  return route.path === path;
};
</script>
