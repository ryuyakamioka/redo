<template>
  <div class="flex flex-col h-full">
    <!-- ページヘッダー -->
    <PageHeader title="品目管理">
      <template #actions>
      </template>
    </PageHeader>

    <!-- メインコンテンツ -->
    <div class="flex-1 overflow-auto bg-gray-50">
      <div class="p-6">
        <!-- 品目登録フォーム -->
        <ItemForm
          :editItem="editingItem"
          @submit="handleSubmit"
          @cancel="handleCancelEdit"
        />

        <!-- 品目一覧 -->
        <ItemList
          :items="items"
          @edit="handleEdit"
          @delete="handleDelete"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useItemStore } from "~/stores/item";
import { computed, ref } from "vue";
import PageHeader from "~/components/layout/PageHeader.vue";
import ItemForm from "~/components/item/ItemForm.vue";
import ItemList from "~/components/item/ItemList.vue";
import type { Item } from "~/types/item";

const itemStore = useItemStore();

// データの取得
await itemStore.fetchItems();

const items = computed(() => itemStore.items);
const editingItem = ref<Item | null>(null);

const handleSubmit = async (formData: any) => {
  try {
    if (editingItem.value) {
      await itemStore.updateItem(editingItem.value.itemId, formData);
      editingItem.value = null;
    } else {
      await itemStore.createItem(formData);
    }
  } catch (error) {
    console.error("品目の保存エラー:", error);
    alert("品目の保存に失敗しました");
  }
};

const handleEdit = (item: Item) => {
  editingItem.value = item;
  window.scrollTo({ top: 0, behavior: 'smooth' });
};

const handleCancelEdit = () => {
  editingItem.value = null;
};

const handleDelete = async (id: number) => {
  try {
    await itemStore.deleteItem(id);
  } catch (error) {
    console.error("品目の削除エラー:", error);
    alert("品目の削除に失敗しました");
  }
};
</script>
