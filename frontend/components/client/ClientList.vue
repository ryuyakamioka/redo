<template>
  <div class="bg-white rounded-lg shadow border border-gray-200">
    <div class="px-4 py-3 border-b border-gray-200 bg-gray-50">
      <h2 class="font-semibold text-gray-700">依頼人一覧</h2>
    </div>
    <div class="overflow-x-auto">
      <table class="w-full">
        <thead class="bg-gray-50 border-b border-gray-200">
          <tr>
            <th
              class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer hover:bg-gray-100"
              @click="sort('clientId')"
            >
              ID
              <span v-if="sortKey === 'clientId'" class="ml-1">{{ sortOrder === 'asc' ? '▲' : '▼' }}</span>
            </th>
            <th
              class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer hover:bg-gray-100"
              @click="sort('clientName')"
            >
              依頼人名
              <span v-if="sortKey === 'clientName'" class="ml-1">{{ sortOrder === 'asc' ? '▲' : '▼' }}</span>
            </th>
            <th
              class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer hover:bg-gray-100"
              @click="sort('clientAbbreviation')"
            >
              略称
              <span v-if="sortKey === 'clientAbbreviation'" class="ml-1">{{ sortOrder === 'asc' ? '▲' : '▼' }}</span>
            </th>
            <th
              class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer hover:bg-gray-100"
              @click="sort('company')"
            >
              会社
              <span v-if="sortKey === 'company'" class="ml-1">{{ sortOrder === 'asc' ? '▲' : '▼' }}</span>
            </th>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              操作
            </th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          <tr
            v-for="client in sortedClients"
            :key="client.clientId"
            class="hover:bg-gray-50 transition-colors"
          >
            <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-700">
              {{ client.clientId }}
            </td>
            <td class="px-4 py-2 text-sm text-gray-900">
              {{ client.clientName }}
            </td>
            <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-700">
              {{ client.clientAbbreviation }}
            </td>
            <td class="px-4 py-2 text-sm text-gray-700">
              {{ client.company.companyName }}
            </td>
            <td class="px-4 py-2 whitespace-nowrap text-sm">
              <button
                @click.stop="handleEdit(client)"
                class="px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors mr-2"
              >
                編集
              </button>
              <button
                @click.stop="handleDelete(client.clientId)"
                class="px-3 py-1 bg-red-600 text-white rounded hover:bg-red-700 transition-colors"
              >
                削除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="clients.length === 0" class="p-8 text-center text-gray-500">
        依頼人が登録されていません
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Client } from "~/types/client";

interface Props {
  clients: Client[];
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "edit", client: Client): void;
  (e: "delete", clientId: number): void;
}>();

const sortKey = ref<string>('');
const sortOrder = ref<'asc' | 'desc'>('asc');

const sortedClients = computed(() => {
  if (!sortKey.value) {
    return props.clients;
  }

  return [...props.clients].sort((a, b) => {
    let aValue: any;
    let bValue: any;

    switch (sortKey.value) {
      case 'clientId':
        aValue = a.clientId;
        bValue = b.clientId;
        break;
      case 'clientName':
        aValue = a.clientName;
        bValue = b.clientName;
        break;
      case 'clientAbbreviation':
        aValue = a.clientAbbreviation || '';
        bValue = b.clientAbbreviation || '';
        break;
      case 'company':
        aValue = a.company.companyName;
        bValue = b.company.companyName;
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

const handleEdit = (client: Client) => {
  emit("edit", client);
};

const handleDelete = (clientId: number) => {
  emit("delete", clientId);
};
</script>
