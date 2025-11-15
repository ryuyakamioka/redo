<template>
  <div class="flex h-screen bg-gray-50">
    <!-- サイドバー -->
    <Sidebar />

    <!-- メインコンテンツエリア -->
    <div class="flex-1 overflow-auto">
      <slot />
    </div>
  </div>
</template>

<script setup lang="ts">
import Sidebar from "~/components/layout/Sidebar.vue";
</script>
