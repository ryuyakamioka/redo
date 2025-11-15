<template>
  <div class="bg-white rounded-lg shadow border border-gray-200">
    <div class="px-4 py-3 border-b border-gray-200 bg-gray-50">
      <h2 class="font-semibold text-gray-700">品目一覧</h2>
    </div>
    <div class="overflow-x-auto">
      <table class="w-full">
        <thead class="bg-gray-50 border-b border-gray-200">
          <tr>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">品目名</th>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">単価</th>
            <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
          <tr v-if="items.length === 0">
            <td colspan="4" class="px-4 py-8 text-center text-gray-500">
              品目が登録されていません
            </td>
          </tr>
          <tr v-for="item in items" :key="item.itemId" class="hover:bg-gray-50">
            <td class="px-4 py-3 text-sm text-gray-900">{{ item.itemId }}</td>
            <td class="px-4 py-3 text-sm text-gray-900">{{ item.itemName }}</td>
            <td class="px-4 py-3 text-sm text-gray-900">¥{{ item.unitPrice.toLocaleString() }}</td>
            <td class="px-4 py-3 text-sm">
              <div class="flex gap-2">
                <button
                  @click.stop="handleEdit(item)"
                  class="px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors text-sm"
                >
                  編集
                </button>
                <button
                  @click.stop="handleDelete(item.itemId)"
                  class="px-3 py-1 bg-red-600 text-white rounded hover:bg-red-700 transition-colors text-sm"
                >
                  削除
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Item } from "~/types/item";

interface Props {
  items: Item[];
}

defineProps<Props>();

const emit = defineEmits<{
  (e: "edit", item: Item): void;
  (e: "delete", id: number): void;
}>();

const handleEdit = (item: Item) => {
  emit("edit", item);
};

const handleDelete = async (id: number) => {
  if (confirm("この品目を削除してもよろしいですか？")) {
    emit("delete", id);
  }
};
</script>
