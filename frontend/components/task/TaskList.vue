<template>
  <div class="bg-white rounded-lg shadow border border-gray-200 overflow-hidden">
    <div v-if="tasks.length > 0" class="overflow-x-auto">
      <table class="min-w-full">
        <thead class="bg-gray-50 border-b border-gray-200">
          <tr>
            <th
              class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200 cursor-pointer hover:bg-gray-100"
              @click="sort('taskTitle')"
            >
              タイトル
              <span v-if="sortKey === 'taskTitle'" class="ml-1">{{ sortOrder === 'asc' ? '▲' : '▼' }}</span>
            </th>
            <th
              class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200 cursor-pointer hover:bg-gray-100"
              @click="sort('client')"
            >
              依頼人
              <span v-if="sortKey === 'client'" class="ml-1">{{ sortOrder === 'asc' ? '▲' : '▼' }}</span>
            </th>
            <th
              class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200 cursor-pointer hover:bg-gray-100"
              @click="sort('requestDate')"
            >
              依頼日
              <span v-if="sortKey === 'requestDate'" class="ml-1">{{ sortOrder === 'asc' ? '▲' : '▼' }}</span>
            </th>
            <th
              class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200 cursor-pointer hover:bg-gray-100"
              @click="sort('expectedDeliveryDate')"
            >
              納品予定日
              <span v-if="sortKey === 'expectedDeliveryDate'" class="ml-1">{{ sortOrder === 'asc' ? '▲' : '▼' }}</span>
            </th>
            <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200">
              品目
            </th>
            <th
              class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200 cursor-pointer hover:bg-gray-100"
              @click="sort('amount')"
            >
              金額
              <span v-if="sortKey === 'amount'" class="ml-1">{{ sortOrder === 'asc' ? '▲' : '▼' }}</span>
            </th>
            <th
              class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider cursor-pointer hover:bg-gray-100"
              @click="sort('taskStatus')"
            >
              ステータス
              <span v-if="sortKey === 'taskStatus'" class="ml-1">{{ sortOrder === 'asc' ? '▲' : '▼' }}</span>
            </th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
          <TaskListItem
            v-for="task in sortedTasks"
            :key="task.taskId"
            :task="task"
            :clients="clients"
            :items="items"
            :is-expanded="expandedTasks.has(task.taskId)"
            @toggle-expanded="toggleTaskDetail"
            @update="handleUpdate"
            @delete="handleDelete"
            @complete="handleComplete"
            @revert="handleRevert"
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

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update", taskId: number, data: any): void;
  (e: "delete", taskId: number): void;
  (e: "complete", taskId: number): void;
  (e: "revert", taskId: number): void;
}>();

const expandedTasks = ref<Set<number>>(new Set());
const sortKey = ref<string>('');
const sortOrder = ref<'asc' | 'desc'>('asc');

const sortedTasks = computed(() => {
  if (!sortKey.value) {
    return props.tasks;
  }

  return [...props.tasks].sort((a, b) => {
    let aValue: any;
    let bValue: any;

    switch (sortKey.value) {
      case 'taskTitle':
        aValue = a.taskTitle.value;
        bValue = b.taskTitle.value;
        break;
      case 'client':
        aValue = a.client?.clientAbbreviation || '';
        bValue = b.client?.clientAbbreviation || '';
        break;
      case 'requestDate':
        aValue = a.requestDate;
        bValue = b.requestDate;
        break;
      case 'expectedDeliveryDate':
        aValue = a.expectedDeliveryDate || '';
        bValue = b.expectedDeliveryDate || '';
        break;
      case 'amount':
        aValue = a.taskItems.reduce((sum, item) => sum + item.amount, 0);
        bValue = b.taskItems.reduce((sum, item) => sum + item.amount, 0);
        break;
      case 'taskStatus':
        aValue = a.taskStatus;
        bValue = b.taskStatus;
        break;
      default:
        return 0;
    }

    if (aValue < bValue) {
      return sortOrder.value === 'asc' ? -1 : 1;
    }
    if (aValue > bValue) {
      return sortOrder.value === 'asc' ? 1 : -1;
    }
    return 0;
  });
});

const sort = (key: string) => {
  if (sortKey.value === key) {
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
  } else {
    sortKey.value = key;
    sortOrder.value = 'asc';
  }
};

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

const handleRevert = (taskId: number) => {
  emit("revert", taskId);
};
</script>
