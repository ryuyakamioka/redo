<template>
  <div class="bg-white rounded-lg shadow border border-gray-200 overflow-hidden">
    <div v-if="tasks.length > 0" class="overflow-x-auto">
      <table class="min-w-full">
        <thead class="bg-gray-50 border-b border-gray-200">
          <tr>
            <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200">
              ID
            </th>
            <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200">
              タイトル
            </th>
            <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200">
              依頼人
            </th>
            <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200">
              依頼日
            </th>
            <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200">
              納品予定日
            </th>
            <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200">
              品目
            </th>
            <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200">
              金額
            </th>
            <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
              ステータス
            </th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
          <TaskListItem
            v-for="task in tasks"
            :key="task.taskId"
            :task="task"
            :clients="clients"
            :items="items"
            :is-expanded="expandedTasks.has(task.taskId)"
            @toggle-expanded="toggleTaskDetail"
            @update="handleUpdate"
            @delete="handleDelete"
            @complete="handleComplete"
          />
        </tbody>
      </table>
    </div>

    <div v-else class="px-6 py-12">
      <div class="text-center">
        <p class="text-gray-500">依頼はありません</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Task } from "~/types/task";
import type { Client } from "~/types/client";
import type { Item } from "~/types/item";
import TaskListItem from "./TaskListItem.vue";

interface Props {
  tasks: Task[];
  clients: Client[];
  items: Item[];
}

defineProps<Props>();
const emit = defineEmits<{
  (e: "update", taskId: number, data: any): void;
  (e: "delete", taskId: number): void;
  (e: "complete", taskId: number): void;
}>();

const expandedTasks = ref<Set<number>>(new Set());

const toggleTaskDetail = (taskId: number) => {
  if (expandedTasks.value.has(taskId)) {
    expandedTasks.value.delete(taskId);
  } else {
    expandedTasks.value.add(taskId);
  }
};

const handleUpdate = (taskId: number, data: any) => {
  emit("update", taskId, data);
};

const handleDelete = (taskId: number) => {
  emit("delete", taskId);
};

const handleComplete = (taskId: number) => {
  emit("complete", taskId);
};
</script>
