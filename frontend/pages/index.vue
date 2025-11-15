<template>
  <div class="min-h-screen bg-gray-50">
    <!-- ヘッダー -->
    <header class="bg-white border-b border-gray-200">
      <div class="container mx-auto px-6 py-4">
        <div class="flex items-center justify-between">
          <h1 class="text-2xl font-bold text-gray-800">依頼管理</h1>
          <div class="text-sm text-gray-600">
            全 <span class="font-semibold text-gray-900">{{ tasks.length }}</span> 件
          </div>
        </div>
      </div>
    </header>

    <!-- メインコンテンツ -->
    <div class="container mx-auto px-6 py-6">
      <!-- 依頼登録フォーム -->
      <div class="mb-6 bg-white rounded-lg shadow border border-gray-200">
        <div class="px-4 py-3 border-b border-gray-200 bg-gray-50">
          <h2 class="font-semibold text-gray-700">新規依頼登録</h2>
        </div>
        <div class="p-4">
          <form @submit.prevent="createTask" class="flex gap-3">
            <input
              type="text"
              v-model="newTaskTitle"
              placeholder="依頼名を入力"
              class="flex-1 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              required
            />
            <select
              v-model="newTaskStatus"
              class="px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-white"
            >
              <option value="TODO">TODO</option>
              <option value="IN_PROGRESS">作業中</option>
              <option value="DONE">完了</option>
            </select>
            <button
              type="submit"
              class="px-6 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors"
            >
              登録
            </button>
          </form>
        </div>
      </div>

      <!-- 依頼テーブル -->
      <div class="bg-white rounded-lg shadow border border-gray-200 overflow-hidden">
        <div v-if="tasks.length > 0" class="overflow-x-auto">
          <table class="min-w-full">
            <thead class="bg-gray-50 border-b border-gray-200">
              <tr>
                <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200">
                  ID
                </th>
                <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200">
                  依頼名
                </th>
                <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200">
                  ステータス
                </th>
                <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                  操作
                </th>
              </tr>
            </thead>
            <tbody class="divide-y divide-gray-200">
              <tr
                v-for="task in tasks"
                :key="task.taskId"
                class="hover:bg-gray-50 transition-colors"
              >
                <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-700 border-r border-gray-200">
                  {{ task.taskId }}
                </td>
                <td class="px-4 py-2 text-sm text-gray-900 border-r border-gray-200">
                  {{ task.taskTitle.value }}
                </td>
                <td class="px-4 py-2 whitespace-nowrap text-sm border-r border-gray-200">
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
                <td class="px-4 py-2 whitespace-nowrap text-sm">
                  <button
                    @click="deleteTask(task.taskId)"
                    class="px-3 py-1 bg-red-600 text-white text-xs rounded hover:bg-red-700 transition-colors"
                  >
                    削除
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div v-else class="px-6 py-12">
          <div class="text-center">
            <p class="text-gray-500">依頼はありません</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useTaskStore } from "~/stores/task";
import { ref, computed } from "vue";

const taskStore = useTaskStore();
await taskStore.fetchTasks();

const newTaskTitle = ref("");
const newTaskStatus = ref<"TODO" | "IN_PROGRESS" | "DONE">("TODO");

const createTask = async () => {
  const task = {
    taskId: 0, // サーバー側で自動採番されるため0を設定
    taskTitle: { value: newTaskTitle.value },
    taskStatus: newTaskStatus.value,
  };
  await taskStore.createTask(task);
  newTaskTitle.value = "";
  newTaskStatus.value = "TODO";
};

const deleteTask = async (taskId: number) => {
  if (confirm('この依頼を削除してもよろしいですか？')) {
    await taskStore.deleteTask(taskId);
  }
};

const tasks = computed(() => taskStore.tasks);
</script>
