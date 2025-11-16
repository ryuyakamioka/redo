<template>
  <div class="mb-6 bg-white rounded-lg shadow border border-gray-200">
    <div class="px-4 py-2 border-b border-gray-200 bg-gray-50">
      <h2 class="font-semibold text-gray-700">{{ isEditMode ? '会社編集' : '新規会社登録' }}</h2>
    </div>
    <div class="p-6">
      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              会社名 <span class="text-red-500">*</span>
            </label>
            <input
              type="text"
              v-model="formData.companyName"
              placeholder="会社名を入力"
              class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">源泉徴収</label>
            <select
              v-model="formData.withholdingTax"
              class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white"
            >
              <option :value="false">なし</option>
              <option :value="true">あり</option>
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
import type { Company } from "~/types/company";

interface Props {
  editCompany?: Company | null;
}

interface FormData {
  companyName: string;
  withholdingTax: boolean;
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "submit", data: any): void;
  (e: "cancel"): void;
}>();

const isEditMode = computed(() => !!props.editCompany);

const formData = ref<FormData>({
  companyName: "",
  withholdingTax: false
});

// 編集モードの場合、フォームに値を設定
watch(() => props.editCompany, (company) => {
  if (company) {
    formData.value = {
      companyName: company.companyName,
      withholdingTax: company.withholdingTax
    };
  } else {
    formData.value = {
      companyName: "",
      withholdingTax: false
    };
  }
}, { immediate: true });

const handleSubmit = () => {
  const data = {
    companyId: props.editCompany ? { value: props.editCompany.companyId } : null,
    companyName: { value: formData.value.companyName },
    withholdingTax: formData.value.withholdingTax
  };

  emit("submit", data);

  // 新規登録の場合のみフォームをリセット
  if (!isEditMode.value) {
    formData.value = {
      companyName: "",
      withholdingTax: false
    };
  }
};

const handleCancel = () => {
  emit("cancel");
};
</script>
