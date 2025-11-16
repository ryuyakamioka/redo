<template>
  <div class="mb-6 bg-white rounded-lg shadow border border-gray-200">
    <div class="px-4 py-2 border-b border-gray-200 bg-gray-50">
      <h2 class="font-semibold text-gray-700">{{ isEditMode ? '品目編集' : '新規品目登録' }}</h2>
    </div>
    <div class="p-6">
      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              品目名 <span class="text-red-500">*</span>
            </label>
            <input
              type="text"
              v-model="formData.itemName"
              placeholder="品目名を入力"
              class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              単価 <span class="text-red-500">*</span>
            </label>
            <input
              type="number"
              v-model.number="formData.unitPrice"
              placeholder="単価を入力"
              min="0"
              step="1"
              class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
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
import type { Item } from "~/types/item";

interface Props {
  editItem?: Item | null;
}

interface FormData {
  itemName: string;
  unitPrice: number;
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "submit", data: any): void;
  (e: "cancel"): void;
}>();

const isEditMode = computed(() => !!props.editItem);

const formData = ref<FormData>({
  itemName: "",
  unitPrice: 0
});

// 編集モードの場合、フォームに値を設定
watch(() => props.editItem, (item) => {
  if (item) {
    formData.value = {
      itemName: item.itemName,
      unitPrice: item.unitPrice
    };
  } else {
    formData.value = {
      itemName: "",
      unitPrice: 0
    };
  }
}, { immediate: true });

const handleSubmit = () => {
  const data = {
    itemId: props.editItem ? { value: props.editItem.itemId } : null,
    itemName: { value: formData.value.itemName },
    unitPrice: { value: formData.value.unitPrice }
  };

  emit("submit", data);

  // 新規登録の場合のみフォームをリセット
  if (!isEditMode.value) {
    formData.value = {
      itemName: "",
      unitPrice: 0
    };
  }
};

const handleCancel = () => {
  emit("cancel");
};
</script>
