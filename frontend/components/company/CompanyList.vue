<template>
  <div class="bg-white rounded-lg shadow border border-gray-200">
    <div class="px-4 py-3 border-b border-gray-200 bg-gray-50">
      <h2 class="font-semibold text-gray-700">会社一覧</h2>
    </div>
    <div class="overflow-x-auto">
      <table class="w-full">
        <thead class="bg-gray-50 border-b border-gray-200">
          <tr>
            <th
              class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer hover:bg-gray-100"
              @click="sort('companyId')"
            >
              ID
              <span v-if="sortKey === 'companyId'" class="ml-1">{{ sortOrder === 'asc' ? '▲' : '▼' }}</span>
            </th>
            <th
              class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer hover:bg-gray-100"
              @click="sort('companyName')"
            >
              会社名
              <span v-if="sortKey === 'companyName'" class="ml-1">{{ sortOrder === 'asc' ? '▲' : '▼' }}</span>
            </th>
            <th
              class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer hover:bg-gray-100"
              @click="sort('withholdingTax')"
            >
              源泉徴収
              <span v-if="sortKey === 'withholdingTax'" class="ml-1">{{ sortOrder === 'asc' ? '▲' : '▼' }}</span>
            </th>
            <th
              class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer hover:bg-gray-100"
              @click="sort('freeePartnerId')"
            >
              freee取引先ID
              <span v-if="sortKey === 'freeePartnerId'" class="ml-1">{{ sortOrder === 'asc' ? '▲' : '▼' }}</span>
            </th>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              操作
            </th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          <tr
            v-for="company in sortedCompanies"
            :key="company.companyId"
            class="hover:bg-gray-50 transition-colors"
          >
            <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-700">
              {{ company.companyId }}
            </td>
            <td class="px-4 py-2 text-sm text-gray-900">
              {{ company.companyName }}
            </td>
            <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-700">
              <span
                :class="company.withholdingTax ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'"
                class="px-2 py-1 rounded-full text-xs"
              >
                {{ company.withholdingTax ? 'あり' : 'なし' }}
              </span>
            </td>
            <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-700">
              {{ company.freeePartnerId || '-' }}
            </td>
            <td class="px-4 py-2 whitespace-nowrap text-sm">
              <button
                @click.stop="handleEdit(company)"
                class="px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors mr-2"
              >
                編集
              </button>
              <button
                @click.stop="handleDelete(company.companyId)"
                class="px-3 py-1 bg-red-600 text-white rounded hover:bg-red-700 transition-colors"
              >
                削除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="companies.length === 0" class="p-8 text-center text-gray-500">
        会社が登録されていません
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Company } from "~/types/company";

interface Props {
  companies: Company[];
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "edit", company: Company): void;
  (e: "delete", companyId: number): void;
}>();

const sortKey = ref<string>('');
const sortOrder = ref<'asc' | 'desc'>('asc');

const sortedCompanies = computed(() => {
  if (!sortKey.value) {
    return props.companies;
  }

  return [...props.companies].sort((a, b) => {
    let aValue: any;
    let bValue: any;

    switch (sortKey.value) {
      case 'companyId':
        aValue = a.companyId;
        bValue = b.companyId;
        break;
      case 'companyName':
        aValue = a.companyName;
        bValue = b.companyName;
        break;
      case 'withholdingTax':
        aValue = a.withholdingTax ? 1 : 0;
        bValue = b.withholdingTax ? 1 : 0;
        break;
      case 'freeePartnerId':
        aValue = a.freeePartnerId ?? 0;
        bValue = b.freeePartnerId ?? 0;
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

const handleEdit = (company: Company) => {
  emit("edit", company);
};

const handleDelete = (companyId: number) => {
  emit("delete", companyId);
};
</script>
