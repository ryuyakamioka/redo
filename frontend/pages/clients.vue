<template>
  <div class="flex flex-col h-full">
    <!-- ページヘッダー -->
    <PageHeader title="依頼人管理">
      <template #actions>
      </template>
    </PageHeader>

    <!-- メインコンテンツ -->
    <div class="flex-1 overflow-auto bg-gray-50">
      <div class="p-6">
        <!-- 依頼人登録フォーム -->
        <ClientForm
          :editClient="editingClient"
          :companies="companies"
          @submit="handleSubmit"
          @cancel="handleCancelEdit"
        />

        <!-- 依頼人一覧 -->
        <ClientList
          :clients="clients"
          @edit="handleEdit"
          @delete="handleDelete"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useClientStore } from "~/stores/client";
import { useCompanyStore } from "~/stores/company";
import { computed, ref } from "vue";
import PageHeader from "~/components/layout/PageHeader.vue";
import ClientForm from "~/components/client/ClientForm.vue";
import ClientList from "~/components/client/ClientList.vue";
import type { Client } from "~/types/client";

const clientStore = useClientStore();
const companyStore = useCompanyStore();

// データを取得
await Promise.all([
  clientStore.fetchClients(),
  companyStore.fetchCompanies()
]);

const clients = computed(() => clientStore.clients);
const companies = computed(() => companyStore.companies);
const editingClient = ref<Client | null>(null);

const handleSubmit = async (formData: any) => {
  try {
    if (editingClient.value) {
      // 更新
      await clientStore.updateClient(editingClient.value.clientId, formData);
      editingClient.value = null;
    } else {
      // 新規作成
      await clientStore.createClient(formData);
    }
  } catch (error) {
    console.error("依頼人の保存エラー:", error);
    alert("依頼人の保存に失敗しました");
  }
};

const handleEdit = (client: Client) => {
  editingClient.value = client;
  // ページトップにスクロール
  window.scrollTo({ top: 0, behavior: 'smooth' });
};

const handleCancelEdit = () => {
  editingClient.value = null;
};

const handleDelete = async (clientId: number) => {
  if (confirm('この依頼人を削除してもよろしいですか？')) {
    try {
      await clientStore.deleteClient(clientId);
    } catch (error) {
      console.error("依頼人の削除エラー:", error);
      alert("依頼人の削除に失敗しました");
    }
  }
};
</script>
