<template>
  <aside class="bg-white border-r border-gray-200 flex flex-col transition-all duration-300" :class="isSidebarExpanded ? 'w-64' : 'w-12'">
    <!-- 折りたたみボタン -->
    <div class="p-2 border-b border-gray-200 flex" :class="isSidebarExpanded ? 'justify-end' : 'justify-center'">
      <button
        @click="isSidebarExpanded = !isSidebarExpanded"
        class="p-2 hover:bg-gray-100 rounded transition-colors"
        :title="isSidebarExpanded ? 'メニューを隠す' : 'メニューを表示'"
      >
        <svg class="w-5 h-5 text-gray-600 transition-transform" :class="{ 'rotate-180': !isSidebarExpanded }" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 19l-7-7 7-7m8 14l-7-7 7-7" />
        </svg>
      </button>
    </div>

    <!-- ロゴエリア -->
    <div v-show="isSidebarExpanded" class="p-6 border-b border-gray-200">
      <h1 class="text-xl font-bold text-gray-800">Redo</h1>
      <p class="text-xs text-gray-500 mt-1">依頼管理システム</p>
    </div>

    <!-- ナビゲーションメニュー -->
    <nav class="flex-1 p-2 overflow-y-auto">
      <!-- 展開時 -->
      <div v-if="isSidebarExpanded" class="space-y-1">
        <!-- 依頼管理セクション -->
        <div class="mb-4">
          <div
            @click="isTaskSectionExpanded = !isTaskSectionExpanded"
            class="px-3 py-1 cursor-pointer hover:bg-gray-50 rounded transition-colors flex items-center justify-between"
          >
            <p class="text-xs font-semibold text-gray-400 uppercase tracking-wider">
              依頼管理
            </p>
            <svg
              class="w-3 h-3 transition-transform text-gray-400"
              :class="{ 'rotate-180': isTaskSectionExpanded }"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
            </svg>
          </div>
          <div v-show="isTaskSectionExpanded" class="mt-2">
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
        </div>

        <!-- 請求書管理セクション -->
        <div class="mb-4">
          <div
            @click="isInvoiceSectionExpanded = !isInvoiceSectionExpanded"
            class="px-3 py-1 cursor-pointer hover:bg-gray-50 rounded transition-colors flex items-center justify-between"
          >
            <p class="text-xs font-semibold text-gray-400 uppercase tracking-wider">
              請求書管理
            </p>
            <svg
              class="w-3 h-3 transition-transform text-gray-400"
              :class="{ 'rotate-180': isInvoiceSectionExpanded }"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
            </svg>
          </div>
          <div v-show="isInvoiceSectionExpanded" class="mt-2">
            <NuxtLink
              to="/invoices"
              class="flex items-center px-3 py-2 text-sm font-medium rounded-lg transition-colors"
              :class="isActive('/invoices') ? 'bg-blue-50 text-blue-700' : 'text-gray-700 hover:bg-gray-50'"
            >
              <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
              請求書一覧
            </NuxtLink>
          </div>
        </div>

        <!-- マスタ管理セクション -->
        <div class="mb-4">
          <div
            @click="isMasterSectionExpanded = !isMasterSectionExpanded"
            class="px-3 py-1 cursor-pointer hover:bg-gray-50 rounded transition-colors flex items-center justify-between"
          >
            <p class="text-xs font-semibold text-gray-400 uppercase tracking-wider">
              マスタ管理
            </p>
            <svg
              class="w-3 h-3 transition-transform text-gray-400"
              :class="{ 'rotate-180': isMasterSectionExpanded }"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
            </svg>
          </div>
          <div v-show="isMasterSectionExpanded" class="mt-2 space-y-1">
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
            <NuxtLink
              to="/task-statuses"
              class="flex items-center px-3 py-2 text-sm font-medium rounded-lg transition-colors"
              :class="isActive('/task-statuses') ? 'bg-blue-50 text-blue-700' : 'text-gray-700 hover:bg-gray-50'"
            >
              <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              依頼ステータス
            </NuxtLink>
          </div>
        </div>
      </div>

      <!-- 折りたたみ時: アイコンのみ -->
      <div v-else class="space-y-2">
        <NuxtLink
          to="/"
          class="flex items-center justify-center p-2 rounded-lg transition-colors"
          :class="isActive('/') ? 'bg-blue-50 text-blue-700' : 'text-gray-700 hover:bg-gray-50'"
          :title="'依頼一覧'"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
          </svg>
        </NuxtLink>
        <div class="my-2 border-t border-gray-200"></div>
        <NuxtLink
          to="/invoices"
          class="flex items-center justify-center p-2 rounded-lg transition-colors"
          :class="isActive('/invoices') ? 'bg-blue-50 text-blue-700' : 'text-gray-700 hover:bg-gray-50'"
          :title="'請求書一覧'"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
          </svg>
        </NuxtLink>
        <div class="my-2 border-t border-gray-200"></div>
        <NuxtLink
          to="/companies"
          class="flex items-center justify-center p-2 rounded-lg transition-colors"
          :class="isActive('/companies') ? 'bg-blue-50 text-blue-700' : 'text-gray-700 hover:bg-gray-50'"
          :title="'会社'"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
          </svg>
        </NuxtLink>
        <NuxtLink
          to="/clients"
          class="flex items-center justify-center p-2 rounded-lg transition-colors"
          :class="isActive('/clients') ? 'bg-blue-50 text-blue-700' : 'text-gray-700 hover:bg-gray-50'"
          :title="'依頼人'"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
          </svg>
        </NuxtLink>
        <NuxtLink
          to="/items"
          class="flex items-center justify-center p-2 rounded-lg transition-colors"
          :class="isActive('/items') ? 'bg-blue-50 text-blue-700' : 'text-gray-700 hover:bg-gray-50'"
          :title="'品目'"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" />
          </svg>
        </NuxtLink>
        <NuxtLink
          to="/task-statuses"
          class="flex items-center justify-center p-2 rounded-lg transition-colors"
          :class="isActive('/task-statuses') ? 'bg-blue-50 text-blue-700' : 'text-gray-700 hover:bg-gray-50'"
          :title="'依頼ステータス'"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </NuxtLink>
      </div>
    </nav>

    <!-- フッター -->
    <div v-show="isSidebarExpanded" class="p-4 border-t border-gray-200">
      <p class="text-xs text-gray-500 text-center">© 2025 Redo</p>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router';

const route = useRoute();

const isSidebarExpanded = ref(true);
const isTaskSectionExpanded = ref(true);
const isInvoiceSectionExpanded = ref(true);
const isMasterSectionExpanded = ref(true);

const isActive = (path: string) => {
  return route.path === path;
};
</script>
