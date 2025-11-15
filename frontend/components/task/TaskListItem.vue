<template>
  <tr
    @click="toggleExpanded"
    class="hover:bg-gray-50 transition-colors cursor-pointer"
    :class="{ 'bg-blue-50': isExpanded }"
  >
      <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-700 border-r border-gray-200">
        {{ task.taskId }}
      </td>
      <td class="px-4 py-2 text-sm text-gray-900 border-r border-gray-200">
        <div class="flex items-center">
          <span class="mr-2">{{ isExpanded ? '▼' : '▶' }}</span>
          {{ task.taskTitle.value }}
        </div>
      </td>
      <td class="px-4 py-2 text-sm text-gray-700 border-r border-gray-200">
        {{ task.client ? task.client.clientName : '-' }}
      </td>
      <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-700 border-r border-gray-200">
        {{ task.requestDate || '-' }}
      </td>
      <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-700 border-r border-gray-200">
        {{ task.expectedDeliveryDate || '-' }}
      </td>
      <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-700 border-r border-gray-200 text-right">
        {{ task.taskItems && task.taskItems.length > 0 ? task.taskItems.length : '-' }}
      </td>
      <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-700 border-r border-gray-200 text-right">
        {{ task.taskItems && task.taskItems.length > 0 ? '¥' + calculateTotal(task.taskItems).toLocaleString() : '-' }}
      </td>
      <td class="px-4 py-2 whitespace-nowrap text-sm">
        <span
          :class="{
            'inline-flex items-center px-2 py-1 rounded text-xs font-medium': true,
            'bg-gray-100 text-gray-700': task.taskStatus === 'TODO',
            'bg-blue-100 text-blue-700': task.taskStatus === 'IN_PROGRESS',
            'bg-green-100 text-green-700': task.taskStatus === 'DONE'
          }"
        >
          {{ task.taskStatus === 'IN_PROGRESS' ? '作業中' : task.taskStatus === 'DONE' ? '完了' : 'TODO' }}
        </span>
      </td>
    </tr>
    <!-- メモと品目の詳細行 -->
    <tr v-if="isExpanded" class="bg-gray-50">
      <td colspan="8" class="px-4 py-3">
        <div class="space-y-3">
          <!-- メモ -->
          <div v-if="task.note" class="text-sm">
            <span class="font-medium text-gray-700">メモ:</span>
            <span class="text-gray-600 ml-2">{{ task.note }}</span>
          </div>
          <!-- 品目 -->
          <div v-if="task.taskItems && task.taskItems.length > 0">
            <div class="font-medium text-gray-700 text-sm mb-1">品目:</div>
            <div class="ml-4 space-y-1">
              <div v-for="(item, idx) in task.taskItems" :key="idx" class="text-sm text-gray-600">
                {{ item.item.itemName }} × {{ item.quantity }}件 = {{ item.amount.toLocaleString() }}円
              </div>
              <div class="text-sm font-medium text-gray-700 pt-1 border-t border-gray-300">
                合計: {{ calculateTotal(task.taskItems).toLocaleString() }}円
              </div>
            </div>
          </div>
          <!-- 削除ボタン -->
          <div class="pt-2 border-t border-gray-300">
            <button
              @click.stop="$emit('delete', task.taskId)"
              class="px-3 py-1 bg-red-600 text-white text-xs rounded hover:bg-red-700 transition-colors"
            >
              この依頼を削除
            </button>
          </div>
        </div>
      </td>
    </tr>
</template>

<script setup lang="ts">
import type { Task } from "~/types/task";

interface Props {
  task: Task;
  isExpanded: boolean;
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "toggleExpanded", taskId: number): void;
  (e: "delete", taskId: number): void;
}>();

const toggleExpanded = () => {
  emit("toggleExpanded", props.task.taskId);
};

const calculateTotal = (taskItems: any[]) => {
  return taskItems.reduce((sum, item) => sum + item.amount, 0);
};
</script>
