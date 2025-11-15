<template>
  <div class="flex flex-col h-full">
    <!-- ページヘッダー -->
    <PageHeader title="会社管理">
      <template #actions>
      </template>
    </PageHeader>

    <!-- メインコンテンツ -->
    <div class="flex-1 overflow-auto bg-gray-50">
      <div class="p-6">
        <!-- 会社登録フォーム -->
        <CompanyForm
          :editCompany="editingCompany"
          @submit="handleSubmit"
          @cancel="handleCancelEdit"
        />

        <!-- 会社一覧 -->
        <CompanyList
          :companies="companies"
          @edit="handleEdit"
          @delete="handleDelete"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useCompanyStore } from "~/stores/company";
import { computed, ref } from "vue";
import PageHeader from "~/components/layout/PageHeader.vue";
import CompanyForm from "~/components/company/CompanyForm.vue";
import CompanyList from "~/components/company/CompanyList.vue";
import type { Company } from "~/types/company";

const companyStore = useCompanyStore();

// 会社データを取得
await companyStore.fetchCompanies();

const companies = computed(() => companyStore.companies);
const editingCompany = ref<Company | null>(null);

const handleSubmit = async (formData: any) => {
  try {
    if (editingCompany.value) {
      // 更新
      await companyStore.updateCompany(editingCompany.value.companyId, formData);
      editingCompany.value = null;
    } else {
      // 新規作成
      await companyStore.createCompany(formData);
    }
  } catch (error) {
    console.error("会社の保存エラー:", error);
    alert("会社の保存に失敗しました");
  }
};

const handleEdit = (company: Company) => {
  editingCompany.value = company;
  // ページトップにスクロール
  window.scrollTo({ top: 0, behavior: 'smooth' });
};

const handleCancelEdit = () => {
  editingCompany.value = null;
};

const handleDelete = async (companyId: number) => {
  if (confirm('この会社を削除してもよろしいですか？')) {
    try {
      await companyStore.deleteCompany(companyId);
    } catch (error) {
      console.error("会社の削除エラー:", error);
      alert("会社の削除に失敗しました");
    }
  }
};
</script>
