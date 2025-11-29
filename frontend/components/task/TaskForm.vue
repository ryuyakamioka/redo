<template>
  <div class="mb-4 bg-white rounded-lg shadow border border-gray-200">
    <div
      class="px-4 py-2 border-b border-gray-200 bg-gray-50 cursor-pointer hover:bg-gray-100 transition-colors"
      @click="isFormExpanded = !isFormExpanded"
    >
      <div class="flex items-center justify-between">
        <h2 class="font-semibold text-gray-700">新規依頼登録</h2>
        <svg
          class="w-5 h-5 transition-transform text-gray-600"
          :class="{ 'rotate-180': isFormExpanded }"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
        >
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
        </svg>
      </div>
    </div>
    <div v-show="isFormExpanded" class="p-4">
      <form @submit.prevent="handleSubmit" class="space-y-3">
        <!-- 第1行: 依頼名、依頼人、納品予定日 -->
        <div class="grid grid-cols-1 md:grid-cols-12 gap-4">
          <div class="md:col-span-3">
            <label class="block text-sm font-medium text-gray-700 mb-1">タイトル <span class="text-red-500">*</span></label>
            <input
              type="text"
              v-model="formData.title"
              placeholder="依頼のタイトルを入力"
              class="w-full h-10 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
          <div class="md:col-span-6">
            <label class="block text-sm font-medium text-gray-700 mb-1">依頼人</label>
            <select
              v-model="formData.clientId"
              class="w-full h-10 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white"
            >
              <option :value="null">選択してください</option>
              <option v-for="client in sortedClients" :key="client.clientId" :value="client.clientId">
                {{ client.clientAbbreviation }} - {{ client.clientName }} - {{ client.company.companyName }}
              </option>
            </select>
          </div>
          <div class="md:col-span-3">
            <label class="block text-sm font-medium text-gray-700 mb-1">納品予定日</label>
            <input
              type="date"
              v-model="formData.expectedDeliveryDate"
              class="w-full h-10 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
        </div>

        <!-- 依頼明細 -->
        <div>
          <div class="space-y-2">
            <div v-for="(item, index) in formData.items" :key="index" class="flex gap-2 items-end">
              <div class="flex-1">
                <label v-if="index === 0" class="block text-xs text-gray-600 mb-1">品目</label>
                <select
                  v-model="item.itemId"
                  @change="updateItemPrice(index)"
                  class="w-full h-10 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white"
                >
                  <option :value="null">品目を選択</option>
                  <option v-for="i in items" :key="i.itemId" :value="i.itemId">
                    {{ i.itemName }} ({{ i.unitPrice }}円)
                  </option>
                </select>
              </div>
              <div class="w-24">
                <label v-if="index === 0" class="block text-xs text-gray-600 mb-1">件数</label>
                <input
                  type="number"
                  v-model.number="item.quantity"
                  @input="updateAmount(index)"
                  placeholder="件数"
                  class="w-full h-10 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div class="w-32">
                <label v-if="index === 0" class="block text-xs text-gray-600 mb-1">金額</label>
                <input
                  type="number"
                  v-model.number="item.amount"
                  placeholder="金額"
                  class="w-full h-10 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <button
                type="button"
                @click="removeItem(index)"
                class="px-3 py-2 text-gray-400 hover:text-gray-600 hover:bg-gray-100 rounded transition-colors"
              >
                ×
              </button>
            </div>
            <button
              type="button"
              @click="addItem"
              class="px-3 py-1 text-sm bg-gray-600 text-white rounded hover:bg-gray-700 transition-colors"
            >
              + 明細を追加
            </button>
          </div>
        </div>

        <!-- 詳細オプション（トグル） -->
        <div class="border-t pt-4">
          <button
            type="button"
            @click="showAdvanced = !showAdvanced"
            class="flex items-center text-sm text-gray-600 hover:text-gray-900 transition-colors"
          >
            <svg
              class="w-4 h-4 mr-2 transition-transform"
              :class="{ 'rotate-90': showAdvanced }"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
            </svg>
            詳細オプション
          </button>

          <div v-show="showAdvanced" class="mt-4 space-y-4 pl-6 border-l-2 border-gray-200">
            <!-- 依頼日、ステータス、納品日 -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">依頼日</label>
                <input
                  type="date"
                  v-model="formData.requestDate"
                  class="w-full h-10 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">ステータス</label>
                <select
                  v-model="formData.status"
                  class="w-full h-10 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white"
                >
                  <option value="TODO">TODO</option>
                  <option value="IN_PROGRESS">作業中</option>
                  <option value="DONE">完了</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">納品日</label>
                <input
                  type="date"
                  v-model="formData.deliveryDate"
                  class="w-full h-10 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
            </div>

            <!-- メモ -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">メモ</label>
              <textarea
                v-model="formData.note"
                placeholder="希望記入欄"
                rows="3"
                class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              ></textarea>
            </div>
          </div>
        </div>

        <!-- 登録ボタン -->
        <div class="flex justify-end">
          <button
            type="submit"
            class="px-6 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors"
          >
            依頼を登録
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Client } from "~/types/client";
import type { Item } from "~/types/item";

interface Props {
  clients: Client[];
  items: Item[];
}

interface FormData {
  title: string;
  status: "TODO" | "IN_PROGRESS" | "DONE";
  requestDate: string;
  expectedDeliveryDate: string;
  deliveryDate: string | null;
  clientId: number | null;
  note: string;
  items: Array<{ itemId: number | null; quantity: number; amount: number }>;
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "submit", data: FormData): void;
}>();

const isFormExpanded = ref(true);
const showAdvanced = ref(false);

// 依頼人を略称の昇順でソート
const sortedClients = computed(() => {
  return [...props.clients].sort((a, b) => {
    const aAbbr = a.clientAbbreviation || '';
    const bAbbr = b.clientAbbreviation || '';
    return aAbbr.localeCompare(bAbbr, 'ja');
  });
});

// セッションストレージから前回の依頼人を取得
const getLastClientId = (): number | null => {
  if (typeof window !== 'undefined') {
    const stored = sessionStorage.getItem('lastClientId');
    return stored ? Number(stored) : null;
  }
  return null;
};

// セッションストレージから前回の品目を取得
const getLastItemId = (): number | null => {
  if (typeof window !== 'undefined') {
    const stored = sessionStorage.getItem('lastItemId');
    return stored ? Number(stored) : null;
  }
  return null;
};

// セッションストレージから前回の依頼日を取得
const getLastRequestDate = (): string => {
  if (typeof window !== 'undefined') {
    const stored = sessionStorage.getItem('lastRequestDate');
    return stored || new Date().toISOString().split('T')[0];
  }
  return new Date().toISOString().split('T')[0];
};

// 前回の品目に基づいて初期金額を計算
const getInitialAmount = (): number => {
  const lastItemId = getLastItemId();
  if (lastItemId && props.items.length > 0) {
    const item = props.items.find(i => i.itemId === lastItemId);
    return item ? item.unitPrice : 0;
  }
  return 0;
};

const formData = ref<FormData>({
  title: "",
  status: "TODO",
  requestDate: getLastRequestDate(),
  expectedDeliveryDate: new Date(Date.now() + 3 * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
  deliveryDate: null,
  clientId: getLastClientId(),
  note: "",
  items: [{ itemId: getLastItemId(), quantity: 1, amount: getInitialAmount() }]
});

const addItem = () => {
  formData.value.items.push({ itemId: null, quantity: 1, amount: 0 });
};

const removeItem = (index: number) => {
  formData.value.items.splice(index, 1);
};

const updateItemPrice = (index: number) => {
  const item = formData.value.items[index];
  if (item.itemId) {
    const selectedItem = props.items.find(i => i.itemId === item.itemId);
    if (selectedItem) {
      item.amount = selectedItem.unitPrice * item.quantity;
    }
  }
};

const updateAmount = (index: number) => {
  const item = formData.value.items[index];
  if (item.itemId) {
    const selectedItem = props.items.find(i => i.itemId === item.itemId);
    if (selectedItem) {
      item.amount = selectedItem.unitPrice * item.quantity;
    }
  }
};

const handleSubmit = () => {
  // 依頼人をセッションストレージに保存
  if (formData.value.clientId !== null && typeof window !== 'undefined') {
    sessionStorage.setItem('lastClientId', String(formData.value.clientId));
  }

  // 依頼日をセッションストレージに保存
  if (formData.value.requestDate && typeof window !== 'undefined') {
    sessionStorage.setItem('lastRequestDate', formData.value.requestDate);
  }

  // 1行目の品目をセッションストレージに保存
  if (formData.value.items.length > 0 && formData.value.items[0].itemId !== null && typeof window !== 'undefined') {
    sessionStorage.setItem('lastItemId', String(formData.value.items[0].itemId));
  }

  emit("submit", { ...formData.value });

  // フォームをリセット（依頼人、依頼日、品目1行目は保持）
  const lastClientId = formData.value.clientId;
  const lastRequestDate = formData.value.requestDate;
  const lastItemId = formData.value.items.length > 0 ? formData.value.items[0].itemId : null;

  // 品目の単価を取得して件数1で金額を計算
  let lastAmount = 0;
  if (lastItemId !== null) {
    const selectedItem = props.items.find(i => i.itemId === lastItemId);
    if (selectedItem) {
      lastAmount = selectedItem.unitPrice * 1; // 件数1で計算
    }
  }

  formData.value = {
    title: "",
    status: "TODO",
    requestDate: lastRequestDate,
    expectedDeliveryDate: new Date(Date.now() + 3 * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
    deliveryDate: null,
    clientId: lastClientId,
    note: "",
    items: [{ itemId: lastItemId, quantity: 1, amount: lastAmount }]
  };
};
</script>
