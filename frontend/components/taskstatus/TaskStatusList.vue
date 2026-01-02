<template>
  <div class="bg-white rounded-lg shadow border border-gray-200">
    <div class="px-4 py-3 border-b border-gray-200 bg-gray-50">
      <h2 class="font-semibold text-gray-700">ステータス一覧</h2>
    </div>
    <div class="overflow-x-auto">
      <table class="w-full">
        <thead class="bg-gray-50 border-b border-gray-200">
          <tr>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              コード
            </th>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              表示名
            </th>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              表示色
            </th>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              表示順
            </th>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              完了フラグ
            </th>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              操作
            </th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          <tr
            v-for="status in taskStatuses"
            :key="status.taskStatusId"
            class="hover:bg-gray-50 transition-colors"
          >
            <td class="px-4 py-2 text-sm text-gray-900 font-mono">
              {{ status.taskStatusCode }}
            </td>
            <td class="px-4 py-2 text-sm text-gray-900">
              {{ status.taskStatusName }}
            </td>
            <td class="px-4 py-2 whitespace-nowrap text-sm">
              <span
                :class="getColorClass(status.displayColor)"
                class="px-2 py-1 rounded text-xs font-medium"
              >
                {{ status.displayColor }}
              </span>
            </td>
            <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-700">
              {{ status.displayOrder }}
            </td>
            <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-700">
              <span
                :class="status.isCompleted ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'"
                class="px-2 py-1 rounded-full text-xs"
              >
                {{ status.isCompleted ? '完了' : '未完了' }}
              </span>
            </td>
            <td class="px-4 py-2 whitespace-nowrap text-sm">
              <button
                @click.stop="handleEdit(status)"
                class="px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors mr-2"
              >
                編集
              </button>
              <button
                @click.stop="handleDelete(status.taskStatusId)"
                class="px-3 py-1 bg-red-600 text-white rounded hover:bg-red-700 transition-colors"
              >
                削除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="taskStatuses.length === 0" class="p-8 text-center text-gray-500">
        ステータスが登録されていません
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { TaskStatusMaster } from "~/types/taskstatus";

interface Props {
  taskStatuses: TaskStatusMaster[];
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "edit", taskStatus: TaskStatusMaster): void;
  (e: "delete", taskStatusId: number): void;
}>();

const getColorClass = (color: string) => {
  const colorMap: Record<string, string> = {
    gray: 'bg-gray-100 text-gray-700',
    blue: 'bg-blue-100 text-blue-700',
    green: 'bg-green-100 text-green-700',
    yellow: 'bg-yellow-100 text-yellow-700',
    red: 'bg-red-100 text-red-700'
  };
  return colorMap[color] || 'bg-gray-100 text-gray-700';
};

const handleEdit = (taskStatus: TaskStatusMaster) => {
  emit("edit", taskStatus);
};

const handleDelete = (taskStatusId: number) => {
  emit("delete", taskStatusId);
};
</script>
