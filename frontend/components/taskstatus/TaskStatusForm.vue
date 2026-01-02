<template>
  <div class="mb-6 bg-white rounded-lg shadow border border-gray-200">
    <div class="px-4 py-2 border-b border-gray-200 bg-gray-50">
      <h2 class="font-semibold text-gray-700">{{ isEditMode ? 'ステータス編集' : '新規ステータス登録' }}</h2>
    </div>
    <div class="p-6">
      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              ステータスコード <span class="text-red-500">*</span>
            </label>
            <input
              type="text"
              v-model="formData.taskStatusCode"
              :disabled="isEditMode"
              placeholder="TODO, IN_PROGRESS, DONE等"
              class="w-full h-10 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              :class="{ 'bg-gray-100': isEditMode }"
              required
            />
            <p class="text-xs text-gray-500 mt-1">
              既存データと互換性のあるコードを指定してください
            </p>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              表示名 <span class="text-red-500">*</span>
            </label>
            <input
              type="text"
              v-model="formData.taskStatusName"
              placeholder="TODO、作業中、完了等"
              class="w-full h-10 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              表示色 <span class="text-red-500">*</span>
            </label>
            <select
              v-model="formData.displayColor"
              class="w-full h-10 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white"
            >
              <option value="gray">グレー</option>
              <option value="blue">ブルー</option>
              <option value="green">グリーン</option>
              <option value="yellow">イエロー</option>
              <option value="red">レッド</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              表示順 <span class="text-red-500">*</span>
            </label>
            <input
              type="number"
              v-model.number="formData.displayOrder"
              min="1"
              class="w-full h-10 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">完了フラグ</label>
            <select
              v-model="formData.isCompleted"
              class="w-full h-10 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white"
            >
              <option :value="false">未完了</option>
              <option :value="true">完了</option>
            </select>
          </div>
        </div>

        <div class="flex justify-end gap-2">
          <button
            v-if="isEditMode"
            type="button"
            @click="handleCancel"
            class="px-6 py-2 bg-gray-600 text-white rounded hover:bg-gray-700 transition-colors"
          >
            キャンセル
          </button>
          <button
            type="submit"
            class="px-6 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors"
          >
            {{ isEditMode ? '更新' : '登録' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { TaskStatusMaster } from "~/types/taskstatus";

interface Props {
  editTaskStatus?: TaskStatusMaster | null;
}

interface FormData {
  taskStatusCode: string;
  taskStatusName: string;
  displayColor: string;
  displayOrder: number;
  isCompleted: boolean;
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "submit", data: any): void;
  (e: "cancel"): void;
}>();

const isEditMode = computed(() => !!props.editTaskStatus);

const formData = ref<FormData>({
  taskStatusCode: "",
  taskStatusName: "",
  displayColor: "gray",
  displayOrder: 1,
  isCompleted: false
});

watch(() => props.editTaskStatus, (taskStatus) => {
  if (taskStatus) {
    formData.value = {
      taskStatusCode: taskStatus.taskStatusCode,
      taskStatusName: taskStatus.taskStatusName,
      displayColor: taskStatus.displayColor,
      displayOrder: taskStatus.displayOrder,
      isCompleted: taskStatus.isCompleted
    };
  } else {
    formData.value = {
      taskStatusCode: "",
      taskStatusName: "",
      displayColor: "gray",
      displayOrder: 1,
      isCompleted: false
    };
  }
}, { immediate: true });

const handleSubmit = () => {
  const data = {
    taskStatusId: props.editTaskStatus ? { value: props.editTaskStatus.taskStatusId } : null,
    taskStatusCode: { value: formData.value.taskStatusCode },
    taskStatusName: { value: formData.value.taskStatusName },
    displayColor: { value: formData.value.displayColor },
    displayOrder: { value: formData.value.displayOrder },
    isCompleted: formData.value.isCompleted
  };

  emit("submit", data);

  if (!isEditMode.value) {
    formData.value = {
      taskStatusCode: "",
      taskStatusName: "",
      displayColor: "gray",
      displayOrder: 1,
      isCompleted: false
    };
  }
};

const handleCancel = () => {
  emit("cancel");
};
</script>
