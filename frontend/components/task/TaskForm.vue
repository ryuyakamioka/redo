<template>
  <div class="mb-6 bg-white rounded-lg shadow border border-gray-200">
    <div class="px-4 py-3 border-b border-gray-200 bg-gray-50">
      <h2 class="font-semibold text-gray-700">新規依頼登録</h2>
    </div>
    <div class="p-6">
      <form @submit.prevent="handleSubmit" class="space-y-4">
        <!-- 第1行: 依頼名、依頼人、納品予定日 -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">依頼名 <span class="text-red-500">*</span></label>
            <input
              type="text"
              v-model="formData.title"
              placeholder="依頼名を入力"
              class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">依頼人</label>
            <select
              v-model="formData.clientId"
              class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white"
            >
              <option :value="null">選択してください</option>
              <option v-for="client in clients" :key="client.clientId" :value="client.clientId">
                {{ client.clientName }} ({{ client.clientAbbreviation }}) - {{ client.company.companyName }}
              </option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">納品予定日</label>
            <input
              type="date"
              v-model="formData.expectedDeliveryDate"
              class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
        </div>

        <!-- 依頼明細 -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">依頼明細</label>
          <div class="space-y-2">
            <div v-for="(item, index) in formData.items" :key="index" class="flex gap-2 items-end">
              <div class="flex-1">
                <select
                  v-model="item.itemId"
                  @change="updateItemPrice(index)"
                  class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white"
                >
                  <option :value="null">品目を選択</option>
                  <option v-for="i in items" :key="i.itemId" :value="i.itemId">
                    {{ i.itemName }} ({{ i.unitPrice }}円)
                  </option>
                </select>
              </div>
              <div class="w-24">
                <input
                  type="number"
                  v-model.number="item.quantity"
                  @input="updateAmount(index)"
                  placeholder="件数"
                  class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div class="w-32">
                <input
                  type="number"
                  v-model.number="item.amount"
                  placeholder="金額"
                  class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <button
                type="button"
                @click="removeItem(index)"
                class="px-3 py-2 bg-red-600 text-white rounded hover:bg-red-700 transition-colors"
              >
                削除
              </button>
            </div>
            <button
              type="button"
              @click="addItem"
              class="px-4 py-2 bg-gray-600 text-white rounded hover:bg-gray-700 transition-colors"
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
            <!-- ステータス -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">ステータス</label>
              <select
                v-model="formData.status"
                class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white"
              >
                <option value="TODO">TODO</option>
                <option value="IN_PROGRESS">作業中</option>
                <option value="DONE">完了</option>
              </select>
            </div>

            <!-- 依頼日と納品日 -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">依頼日</label>
                <input
                  type="date"
                  v-model="formData.requestDate"
                  class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">納品日</label>
                <input
                  type="date"
                  v-model="formData.deliveryDate"
                  class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
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
            登録
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

const showAdvanced = ref(false);

const formData = ref<FormData>({
  title: "",
  status: "TODO",
  requestDate: new Date().toISOString().split('T')[0],
  expectedDeliveryDate: new Date(Date.now() + 3 * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
  deliveryDate: null,
  clientId: null,
  note: "",
  items: []
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
  emit("submit", { ...formData.value });

  // フォームをリセット
  formData.value = {
    title: "",
    status: "TODO",
    requestDate: new Date().toISOString().split('T')[0],
    expectedDeliveryDate: new Date(Date.now() + 3 * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
    deliveryDate: null,
    clientId: null,
    note: "",
    items: []
  };
};
</script>
