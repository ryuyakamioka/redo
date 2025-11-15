<template>
  <div class="min-h-screen bg-gray-50">
    <!-- ヘッダー -->
    <header class="bg-white border-b border-gray-200">
      <div class="container mx-auto px-6 py-4">
        <div class="flex items-center justify-between">
          <h1 class="text-2xl font-bold text-gray-800">依頼管理</h1>
          <div class="text-sm text-gray-600">
            全 <span class="font-semibold text-gray-900">{{ tasks.length }}</span> 件
          </div>
        </div>
      </div>
    </header>

    <!-- メインコンテンツ -->
    <div class="container mx-auto px-6 py-6">
      <!-- 依頼登録フォーム -->
      <div class="mb-6 bg-white rounded-lg shadow border border-gray-200">
        <div class="px-4 py-3 border-b border-gray-200 bg-gray-50">
          <h2 class="font-semibold text-gray-700">新規依頼登録</h2>
        </div>
        <div class="p-6">
          <form @submit.prevent="createTask" class="space-y-4">
            <!-- 基本情報 -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">依頼名 <span class="text-red-500">*</span></label>
                <input
                  type="text"
                  v-model="newTask.title"
                  placeholder="依頼名を入力"
                  class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">ステータス</label>
                <select
                  v-model="newTask.status"
                  class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white"
                >
                  <option value="TODO">TODO</option>
                  <option value="IN_PROGRESS">作業中</option>
                  <option value="DONE">完了</option>
                </select>
              </div>
            </div>

            <!-- 日付と依頼人 -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">依頼日</label>
                <input
                  type="date"
                  v-model="newTask.requestDate"
                  class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">納品予定日</label>
                <input
                  type="date"
                  v-model="newTask.expectedDeliveryDate"
                  class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">納品日</label>
                <input
                  type="date"
                  v-model="newTask.deliveryDate"
                  class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
            </div>

            <!-- 依頼人選択 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">依頼人</label>
              <select
                v-model="newTask.clientId"
                class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white"
              >
                <option :value="null">選択してください</option>
                <option v-for="client in clients" :key="client.clientId" :value="client.clientId">
                  {{ client.clientName }} ({{ client.clientAbbreviation }}) - {{ client.company.companyName }}
                </option>
              </select>
            </div>

            <!-- メモ -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">メモ</label>
              <textarea
                v-model="newTask.note"
                placeholder="希望記入欄"
                rows="3"
                class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              ></textarea>
            </div>

            <!-- 依頼明細 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">依頼明細</label>
              <div class="space-y-2">
                <div v-for="(item, index) in newTask.items" :key="index" class="flex gap-2 items-end">
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

      <!-- 依頼テーブル -->
      <div class="bg-white rounded-lg shadow border border-gray-200 overflow-hidden">
        <div v-if="tasks.length > 0" class="overflow-x-auto">
          <table class="min-w-full">
            <thead class="bg-gray-50 border-b border-gray-200">
              <tr>
                <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200">
                  ID
                </th>
                <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200">
                  依頼名
                </th>
                <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200">
                  依頼人
                </th>
                <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200">
                  依頼日
                </th>
                <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200">
                  納品予定日
                </th>
                <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider border-r border-gray-200">
                  ステータス
                </th>
                <th class="px-4 py-2 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                  操作
                </th>
              </tr>
            </thead>
            <tbody class="divide-y divide-gray-200">
              <tr
                v-for="task in tasks"
                :key="task.taskId"
                class="hover:bg-gray-50 transition-colors"
              >
                <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-700 border-r border-gray-200">
                  {{ task.taskId }}
                </td>
                <td class="px-4 py-2 text-sm text-gray-900 border-r border-gray-200">
                  {{ task.taskTitle.value }}
                </td>
                <td class="px-4 py-2 text-sm text-gray-700 border-r border-gray-200">
                  {{ task.client ? task.client.clientName : '-' }}
                </td>
                <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-700 border-r border-gray-200">
                  {{ task.requestDate || '-' }}
                </td>
                <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-700 border-r border-gray-200">
                  {{ task.expectedDeliveryDate || '-' }}
                </td>
                <td class="px-4 py-2 whitespace-nowrap text-sm border-r border-gray-200">
                  <span
                    :class="{
                      'inline-flex items-center px-2 py-1 rounded text-xs font-medium': true,
                      'bg-gray-100 text-gray-700': task.taskStatus === 'TODO',
                      'bg-blue-100 text-blue-700': task.taskStatus === 'IN_PROGRESS',
                      'bg-green-100 text-green-700': task.taskStatus === 'DONE'
                    }"
                  >
                    {{ task.taskStatus === 'IN_PROGRESS' ? '作業中' : task.taskStatus === 'DONE' ? '完了' : 'TODO' }}
                  </span>
                </td>
                <td class="px-4 py-2 whitespace-nowrap text-sm">
                  <button
                    @click="deleteTask(task.taskId)"
                    class="px-3 py-1 bg-red-600 text-white text-xs rounded hover:bg-red-700 transition-colors"
                  >
                    削除
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div v-else class="px-6 py-12">
          <div class="text-center">
            <p class="text-gray-500">依頼はありません</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useTaskStore } from "~/stores/task";
import { useClientStore } from "~/stores/client";
import { useItemStore } from "~/stores/item";
import { ref, computed, onMounted } from "vue";

const taskStore = useTaskStore();
const clientStore = useClientStore();
const itemStore = useItemStore();

// マスタデータと依頼データを取得
await Promise.all([
  taskStore.fetchTasks(),
  clientStore.fetchClients(),
  itemStore.fetchItems()
]);

// フォーム用の状態
const newTask = ref({
  title: "",
  status: "TODO" as "TODO" | "IN_PROGRESS" | "DONE",
  requestDate: new Date().toISOString().split('T')[0], // 今日の日付をデフォルト
  expectedDeliveryDate: new Date(Date.now() + 3 * 24 * 60 * 60 * 1000).toISOString().split('T')[0], // 3日後をデフォルト
  deliveryDate: null as string | null,
  clientId: null as number | null,
  note: "",
  items: [] as Array<{ itemId: number | null; quantity: number; amount: number }>
});

const clients = computed(() => clientStore.clients);
const items = computed(() => itemStore.items);
const tasks = computed(() => taskStore.tasks);

// 明細行を追加
const addItem = () => {
  newTask.value.items.push({ itemId: null, quantity: 1, amount: 0 });
};

// 明細行を削除
const removeItem = (index: number) => {
  newTask.value.items.splice(index, 1);
};

// 品目変更時に単価を自動設定
const updateItemPrice = (index: number) => {
  const item = newTask.value.items[index];
  if (item.itemId) {
    const selectedItem = items.value.find(i => i.itemId === item.itemId);
    if (selectedItem) {
      item.amount = selectedItem.unitPrice * item.quantity;
    }
  }
};

// 件数変更時に金額を再計算
const updateAmount = (index: number) => {
  const item = newTask.value.items[index];
  if (item.itemId) {
    const selectedItem = items.value.find(i => i.itemId === item.itemId);
    if (selectedItem) {
      item.amount = selectedItem.unitPrice * item.quantity;
    }
  }
};

const createTask = async () => {
  // 選択された依頼人を取得
  const selectedClient = clients.value.find(c => c.clientId === newTask.value.clientId);

  // 依頼明細を構築
  const taskItems = newTask.value.items
    .filter(item => item.itemId !== null)
    .map(item => {
      const selectedItem = items.value.find(i => i.itemId === item.itemId);
      return {
        taskItemId: null,
        item: selectedItem!,
        quantity: { value: item.quantity },
        amount: { value: item.amount }
      };
    });

  const task = {
    taskId: 0,
    taskTitle: { value: newTask.value.title },
    taskStatus: newTask.value.status,
    requestDate: newTask.value.requestDate,
    client: selectedClient || null,
    note: newTask.value.note || null,
    taskItems: taskItems,
    expectedDeliveryDate: newTask.value.expectedDeliveryDate,
    deliveryDate: newTask.value.deliveryDate
  };

  await taskStore.createTask(task);

  // フォームをリセット
  newTask.value = {
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

const deleteTask = async (taskId: number) => {
  if (confirm('この依頼を削除してもよろしいですか？')) {
    await taskStore.deleteTask(taskId);
  }
};
</script>
