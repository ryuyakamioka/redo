<template>
  <div class="mb-6 bg-white rounded-lg shadow border border-gray-200">
    <div class="px-4 py-2 border-b border-gray-200 bg-gray-50">
      <h2 class="font-semibold text-gray-700">{{ isEditMode ? '依頼人編集' : '新規依頼人登録' }}</h2>
    </div>
    <div class="p-6">
      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              依頼人名 <span class="text-red-500">*</span>
            </label>
            <input
              type="text"
              v-model="formData.clientName"
              placeholder="依頼人名を入力"
              class="w-full h-10 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              略称 <span class="text-red-500">*</span>
            </label>
            <input
              type="text"
              v-model="formData.clientAbbreviation"
              placeholder="略称を入力"
              class="w-full h-10 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">
              会社 <span class="text-red-500">*</span>
            </label>
            <select
              v-model="formData.companyId"
              class="w-full h-10 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white"
              required
            >
              <option :value="null">選択してください</option>
              <option v-for="company in companies" :key="company.companyId" :value="company.companyId">
                {{ company.companyName }}
              </option>
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
import type { Client } from "~/types/client";
import type { Company } from "~/types/company";

interface Props {
  editClient?: Client | null;
  companies: Company[];
}

interface FormData {
  clientName: string;
  clientAbbreviation: string;
  companyId: number | null;
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "submit", data: any): void;
  (e: "cancel"): void;
}>();

const isEditMode = computed(() => !!props.editClient);

const formData = ref<FormData>({
  clientName: "",
  clientAbbreviation: "",
  companyId: null
});

// 編集モードの場合、フォームに値を設定
watch(() => props.editClient, (client) => {
  if (client) {
    formData.value = {
      clientName: client.clientName,
      clientAbbreviation: client.clientAbbreviation,
      companyId: client.company.companyId
    };
  } else {
    formData.value = {
      clientName: "",
      clientAbbreviation: "",
      companyId: null
    };
  }
}, { immediate: true });

const handleSubmit = () => {
  const selectedCompany = props.companies.find(c => c.companyId === formData.value.companyId);
  if (!selectedCompany) {
    alert("会社を選択してください");
    return;
  }

  const data = {
    clientId: props.editClient ? { value: props.editClient.clientId } : null,
    clientName: { value: formData.value.clientName },
    clientAbbreviation: { value: formData.value.clientAbbreviation },
    company: {
      companyId: { value: selectedCompany.companyId },
      companyName: { value: selectedCompany.companyName },
      withholdingTax: selectedCompany.withholdingTax
    }
  };

  emit("submit", data);

  // 新規登録の場合のみフォームをリセット
  if (!isEditMode.value) {
    formData.value = {
      clientName: "",
      clientAbbreviation: "",
      companyId: null
    };
  }
};

const handleCancel = () => {
  emit("cancel");
};
</script>
