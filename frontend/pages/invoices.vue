<template>
  <div class="flex flex-col h-full">
    <!-- ページヘッダー -->
    <PageHeader title="請求書管理">
      <template #actions>
      </template>
    </PageHeader>

    <!-- メインコンテンツ -->
    <div class="flex-1 overflow-auto bg-gray-50">
      <div class="p-6">
        <!-- 年月選択フォーム -->
        <div class="mb-4 bg-white rounded-lg shadow border border-gray-200 p-4">
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-4">
              <div>
                <input
                  type="month"
                  v-model="selectedMonth"
                  @change="fetchEstimates"
                  class="px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div class="flex items-center gap-2">
                <input
                  type="checkbox"
                  id="showOnlyUnbilled"
                  v-model="showOnlyUnbilled"
                  class="w-4 h-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500"
                />
                <label for="showOnlyUnbilled" class="text-sm text-gray-700 cursor-pointer">
                  未請求のみ表示
                </label>
              </div>
            </div>
            <div class="text-sm text-gray-600">
              表示中の請求予定: <span class="font-medium text-gray-700">{{ summary.count }}件</span> <span class="font-medium text-gray-700">¥{{ summary.total.toLocaleString() }}</span>
            </div>
          </div>
        </div>

        <!-- freee連携ボタン -->
        <div class="mb-4 flex gap-2 justify-end">
          <button
            @click="handleFreeeAuth"
            class="px-3 py-1.5 text-sm bg-green-600 text-white rounded hover:bg-green-700 transition-colors"
          >
            freee認証
          </button>
          <button
            @click="handleFreeeSendInvoice"
            class="px-3 py-1.5 text-sm bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors"
          >
            freee請求書送信
          </button>
        </div>

        <!-- 請求予定リスト -->
        <div v-if="estimates.length > 0" class="space-y-4">
          <div
            v-for="estimate in estimates"
            :key="estimate.companyId"
            class="bg-white rounded-lg shadow border border-gray-200 overflow-hidden"
          >
            <!-- 会社ヘッダー -->
            <div class="px-4 py-3 bg-gray-50 border-b border-gray-200">
              <div class="flex items-center justify-between">
                <div class="flex items-center gap-3">
                  <input
                    type="checkbox"
                    :id="`estimate-${estimate.companyId}`"
                    v-model="selectedCompanyIds"
                    :value="estimate.companyId"
                    class="w-4 h-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500"
                  />
                  <label :for="`estimate-${estimate.companyId}`" class="font-semibold text-gray-800 cursor-pointer">
                    {{ getCompanyName(estimate.companyId) }}
                  </label>
                </div>
                <div class="text-lg font-bold text-blue-600">
                  小計: ¥{{ estimate.subtotal.toLocaleString() }}
                </div>
              </div>
              <div class="text-sm text-gray-600 mt-1 ml-7">
                計上年月: {{ estimate.year }}年{{ estimate.month }}月
              </div>
            </div>

            <!-- 明細テーブル -->
            <div class="overflow-x-auto">
              <table class="min-w-full">
                <thead class="bg-gray-50 border-b border-gray-200">
                  <tr>
                    <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                      摘要
                    </th>
                    <th class="px-4 py-2 text-right text-xs font-semibold text-gray-600 uppercase tracking-wider">
                      数量
                    </th>
                    <th class="px-4 py-2 text-right text-xs font-semibold text-gray-600 uppercase tracking-wider">
                      単価
                    </th>
                    <th class="px-4 py-2 text-right text-xs font-semibold text-gray-600 uppercase tracking-wider">
                      金額
                    </th>
                  </tr>
                </thead>
                <tbody class="divide-y divide-gray-200">
                  <tr v-for="(item, index) in estimate.items" :key="index" class="hover:bg-gray-50">
                    <td class="px-4 py-2 text-sm text-gray-900">{{ item.description }}</td>
                    <td class="px-4 py-2 text-sm text-gray-700 text-right">{{ item.quantity }}</td>
                    <td class="px-4 py-2 text-sm text-gray-700 text-right">¥{{ item.unitPrice.toLocaleString() }}</td>
                    <td class="px-4 py-2 text-sm text-gray-900 text-right font-medium">
                      ¥{{ item.totalPrice.toLocaleString() }}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <!-- 空の状態 -->
        <div v-else class="bg-white rounded-lg shadow border border-gray-200 px-6 py-12">
          <div class="text-center">
            <p class="text-gray-500">請求予定データがありません</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useInvoiceStore } from "~/stores/invoice";
import { useCompanyStore } from "~/stores/company";
import { computed, ref, watch } from "vue";
import PageHeader from "~/components/layout/PageHeader.vue";

const invoiceStore = useInvoiceStore();
const companyStore = useCompanyStore();

// 会社一覧を取得
await companyStore.fetchCompanies();

const companies = computed(() => companyStore.companies);
const estimates = computed(() => invoiceStore.estimates);

// 送信対象の会社IDリスト
const selectedCompanyIds = ref<number[]>([]);

// 未請求のみ表示フラグ
const showOnlyUnbilled = ref<boolean>(true);

// 請求予定のサマリー
const summary = computed(() => {
  const count = estimates.value.length;
  const total = estimates.value.reduce((sum, estimate) => sum + estimate.subtotal, 0);
  return { count, total };
});

// 現在の年月をデフォルト値として設定
const now = new Date();
const defaultMonth = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`;

const selectedMonth = ref<string>(defaultMonth);

const fetchEstimates = async () => {
  if (!selectedMonth.value) return;

  try {
    // YYYY-MM形式からYYYYMMに変換
    const billingMonth = selectedMonth.value.replace('-', '');

    await invoiceStore.fetchEstimates(billingMonth, undefined, showOnlyUnbilled.value);
  } catch (error) {
    console.error("請求予定の取得に失敗しました:", error);
    alert("請求予定の取得に失敗しました");
  }
};

const getCompanyName = (companyId: number): string => {
  const company = companies.value.find(c => c.companyId === companyId);
  return company ? company.companyName : `会社ID: ${companyId}`;
};

// freee認証ハンドラー
const handleFreeeAuth = async () => {
  try {
    const response = await useApi().get('/freee/auth-url');
    const authUrl = response.data.authUrl;

    // 認証URLを新しいウィンドウで開く
    window.open(authUrl, '_blank', 'width=600,height=700');
  } catch (error) {
    console.error('freee認証URLの取得に失敗しました:', error);
    alert('freee認証URLの取得に失敗しました');
  }
};

// freee請求書送信ハンドラー
const handleFreeeSendInvoice = async () => {
  if (selectedCompanyIds.value.length === 0) {
    alert('送信する請求書を選択してください');
    return;
  }

  if (!confirm(`${selectedCompanyIds.value.length}件の請求書をfreeeに送信しますか？`)) {
    return;
  }

  // billingMonth形式に変換（YYYYMM）
  const billingMonth = selectedMonth.value.replace('-', '');

  try {
    const requestBody = {
      billingMonth: billingMonth,
      companyIds: selectedCompanyIds.value
    };

    const response = await useApi().post('/freee/invoices/send', requestBody);

    if (response.status === 200) {
      alert(`${selectedCompanyIds.value.length}件の請求書をfreeeに送信しました`);
      selectedCompanyIds.value = []; // 選択をクリア
    }
  } catch (error: any) {
    console.error('freee請求書送信エラー:', error);
    const errorMessage = error.response?.data?.message || 'freee請求書送信に失敗しました';
    alert(errorMessage);
  }
};

// 未請求フラグの変更を監視
watch(showOnlyUnbilled, () => {
  fetchEstimates();
});

// 初回ロード時に自動で検索
await fetchEstimates();
</script>
