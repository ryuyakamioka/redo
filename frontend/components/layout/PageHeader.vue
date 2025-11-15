<template>
  <header class="bg-white border-b border-gray-200">
    <div class="px-6 py-4">
      <div class="flex items-center justify-between">
        <div>
          <h1 class="text-2xl font-bold text-gray-800">{{ title }}</h1>
          <p v-if="description" class="text-sm text-gray-600 mt-1">{{ description }}</p>
        </div>
        <div v-if="$slots.actions" class="flex items-center gap-2">
          <slot name="actions" />
        </div>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
interface Props {
  title: string;
  description?: string;
}

defineProps<Props>();
</script>
