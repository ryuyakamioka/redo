<template>
  <tr
    @click="toggleExpanded"
    class="hover:bg-gray-50 transition-colors cursor-pointer"
    :class="{ 'bg-blue-50': isExpanded }"
  >
      <td class="px-4 py-2 text-sm text-gray-900 border-r border-gray-200">
        <div class="flex items-center">
          <span class="mr-2">{{ isExpanded ? '▼' : '▶' }}</span>
          {{ task.taskTitle.value }}
        </div>
      </td>
      <td class="px-4 py-2 text-sm text-gray-700 border-r border-gray-200">
        {{ task.client ? task.client.clientAbbreviation : '-' }}
      </td>
      <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-700 border-r border-gray-200">
        {{ task.requestDate || '-' }}
      </td>
      <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-700 border-r border-gray-200">
        {{ task.expectedDeliveryDate || '-' }}
      </td>
      <td class="px-4 py-2 text-sm text-gray-700 border-r border-gray-200">
        {{ task.taskItems && task.taskItems.length > 0 ? task.taskItems.map(item => `${item.item.itemName}: ${item.quantity}件`).join(' / ') : '-' }}
      </td>
      <td class="px-4 py-2 whitespace-nowrap text-sm text-gray-700 border-r border-gray-200 text-right">
        {{ task.taskItems && task.taskItems.length > 0 ? '¥' + calculateTotal(task.taskItems).toLocaleString() : '-' }}
      </td>
      <td class="px-4 py-2 whitespace-nowrap text-sm" @click.stop>
        <select
          v-if="task.taskStatus !== 'DONE'"
          v-model="localStatus"
          @change="handleStatusChange"
          :class="{
            'px-2 py-1 rounded text-xs font-medium border-0 cursor-pointer': true,
            'bg-gray-100 text-gray-700': task.taskStatus === 'TODO',
            'bg-blue-100 text-blue-700': task.taskStatus === 'IN_PROGRESS'
          }"
        >
          <option value="TODO">TODO</option>
          <option value="IN_PROGRESS">作業中</option>
        </select>
        <span
          v-else
          class="inline-flex items-center px-2 py-1 rounded text-xs font-medium bg-green-100 text-green-700"
        >
          完了
        </span>
      </td>
    </tr>
    <!-- メモと品目の詳細行 / 編集フォーム -->
    <tr v-if="isExpanded" class="bg-gray-50">
      <td colspan="7" class="px-4 py-3" @click.stop>
        <!-- 表示モード -->
        <div v-if="!isEditMode" class="space-y-3">
          <!-- メモ -->
          <div v-if="task.note" class="text-sm">
            <span class="font-medium text-gray-700">メモ:</span>
            <span class="text-gray-600 ml-2">{{ task.note }}</span>
          </div>
          <!-- 品目 -->
          <div v-if="task.taskItems && task.taskItems.length > 0">
            <div class="font-medium text-gray-700 text-sm mb-1">品目:</div>
            <div class="ml-4 space-y-1">
              <div v-for="(item, idx) in task.taskItems" :key="idx" class="text-sm text-gray-600">
                {{ item.item.itemName }} × {{ item.quantity }}件 = {{ item.amount.toLocaleString() }}円
              </div>
              <div class="text-sm font-medium text-gray-700 pt-1 border-t border-gray-300">
                合計: {{ calculateTotal(task.taskItems).toLocaleString() }}円
              </div>
            </div>
          </div>
          <!-- ボタン -->
          <div class="pt-2 border-t border-gray-300 flex gap-2">
            <button
              v-if="task.taskStatus !== 'DONE'"
              @click="startEdit"
              class="px-3 py-1 bg-blue-600 text-white text-xs rounded hover:bg-blue-700 transition-colors"
            >
              この依頼を編集
            </button>
            <button
              v-if="task.taskStatus !== 'DONE'"
              @click="$emit('delete', task.taskId)"
              class="px-3 py-1 bg-red-600 text-white text-xs rounded hover:bg-red-700 transition-colors"
            >
              この依頼を削除
            </button>
            <button
              v-if="task.taskStatus !== 'DONE'"
              @click="showCompleteDialog = true"
              class="px-3 py-1 bg-green-600 text-white text-xs rounded hover:bg-green-700 transition-colors"
            >
              この依頼を完了する
            </button>
            <button
              v-if="task.taskStatus === 'DONE'"
              @click="$emit('revert', task.taskId)"
              class="px-3 py-1 bg-yellow-600 text-white text-xs rounded hover:bg-yellow-700 transition-colors"
            >
              作業中に戻す
            </button>
            <div v-if="task.taskStatus === 'DONE'" class="text-sm text-gray-600 py-1">
              完了済みの依頼は編集・削除できません
            </div>
          </div>
        </div>

        <!-- 編集モード -->
        <div v-else class="space-y-4">
          <div class="text-sm font-semibold text-gray-700 mb-3">依頼の編集</div>

          <!-- 第1行: 依頼名、依頼人、納品予定日 -->
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">依頼名 <span class="text-red-500">*</span></label>
              <input
                type="text"
                v-model="formData.title"
                class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                required
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">依頼人</label>
              <select
                v-model="formData.clientId"
                class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white text-sm"
              >
                <option :value="null">選択してください</option>
                <option v-for="client in clients" :key="client.clientId" :value="client.clientId">
                  {{ client.clientName }} ({{ client.clientAbbreviation }})
                </option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">着手予定日</label>
              <input
                type="date"
                v-model="formData.expectedDeliveryDate"
                class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"
              />
            </div>
          </div>

          <!-- 依頼明細 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">依頼明細</label>
            <div class="space-y-2">
              <div v-for="(item, index) in formData.items" :key="index" class="flex gap-2 items-end">
                <div class="flex-1">
                  <label v-if="index === 0" class="block text-xs text-gray-600 mb-1">品目</label>
                  <select
                    v-model="item.itemId"
                    @change="updateItemPrice(index)"
                    class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white text-sm"
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
                    class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                  />
                </div>
                <div class="w-32">
                  <label v-if="index === 0" class="block text-xs text-gray-600 mb-1">金額</label>
                  <input
                    type="number"
                    v-model.number="item.amount"
                    class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                  />
                </div>
                <button
                  type="button"
                  @click="removeItem(index)"
                  class="px-3 py-2 text-gray-400 hover:text-gray-600 hover:bg-gray-100 rounded transition-colors text-xs"
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

          <!-- 詳細オプション -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">ステータス</label>
              <select
                v-model="formData.status"
                class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white text-sm"
              >
                <option value="TODO">TODO</option>
                <option value="IN_PROGRESS">作業中</option>
                <option value="DONE">完了</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">依頼日</label>
              <input
                type="date"
                v-model="formData.requestDate"
                class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"
              />
            </div>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">メモ</label>
            <textarea
              v-model="formData.note"
              rows="3"
              class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"
            ></textarea>
          </div>

          <!-- ボタン -->
          <div class="flex justify-end gap-2 pt-2 border-t border-gray-300">
            <button
              @click="cancelEdit"
              class="px-4 py-2 bg-gray-600 text-white text-sm rounded hover:bg-gray-700 transition-colors"
            >
              キャンセル
            </button>
            <button
              @click="saveEdit"
              class="px-4 py-2 bg-blue-600 text-white text-sm rounded hover:bg-blue-700 transition-colors"
            >
              保存
            </button>
          </div>
        </div>
      </td>
    </tr>

    <!-- 完了確認ダイアログ -->
    <tr v-if="showCompleteDialog">
      <td colspan="7" class="p-0">
        <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" @click.stop="closeCompleteDialog">
          <div class="bg-white rounded-lg p-6 max-w-md w-full mx-4" @click.stop>
            <h3 class="text-lg font-semibold text-gray-900 mb-4">依頼を完了しますか?</h3>
            <div class="space-y-3 mb-6">
              <!-- 品目一覧 -->
              <div v-if="task.taskItems && task.taskItems.length > 0" class="bg-gray-50 rounded p-3">
                <div class="font-medium text-gray-700 text-sm mb-2">納品内容</div>
                <div class="space-y-1">
                  <div v-for="(item, idx) in task.taskItems" :key="idx" class="text-sm text-gray-600">
                    {{ item.item.itemName }} × {{ item.quantity }}件 = {{ item.amount.toLocaleString() }}円
                  </div>
                  <div class="text-sm font-medium text-gray-900 pt-2 border-t border-gray-300">
                    合計: {{ calculateTotal(task.taskItems).toLocaleString() }}円
                  </div>
                </div>
              </div>
              <div class="pt-3 border-t border-gray-200">
                <label class="flex items-start cursor-pointer">
                  <input
                    type="checkbox"
                    v-model="confirmChecked"
                    class="mt-1 mr-2"
                  />
                  <span class="text-sm text-gray-700">
                    納品件数と金額が正しいことを確認しました
                  </span>
                </label>
              </div>
            </div>
            <div class="flex justify-end gap-2">
              <button
                @click="closeCompleteDialog"
                class="px-4 py-2 bg-gray-600 text-white text-sm rounded hover:bg-gray-700 transition-colors"
              >
                キャンセル
              </button>
              <button
                @click="handleComplete"
                :disabled="!confirmChecked"
                :class="[
                  'px-4 py-2 text-white text-sm rounded transition-colors',
                  confirmChecked
                    ? 'bg-green-600 hover:bg-green-700'
                    : 'bg-gray-300 cursor-not-allowed'
                ]"
              >
                完了する
              </button>
            </div>
          </div>
        </div>
      </td>
    </tr>

    <!-- 完了成功ダイアログ -->
    <tr v-if="showSuccessDialog">
      <td colspan="7" class="p-0">
        <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" @click.stop="showSuccessDialog = false">
          <div class="bg-white rounded-lg p-8 max-w-sm w-full mx-4" @click.stop>
            <div class="text-center">
              <!-- チェックマークアイコン -->
              <div class="mx-auto flex items-center justify-center h-20 w-20 rounded-full bg-green-100 mb-4">
                <svg class="h-12 w-12 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                </svg>
              </div>
              <h3 class="text-2xl font-bold text-gray-900 mb-2">おつかれさまでした！</h3>
              <p class="text-sm text-gray-600 mb-6">依頼が完了しました</p>
              <button
                @click="showSuccessDialog = false"
                class="px-6 py-2 bg-green-600 text-white text-sm rounded hover:bg-green-700 transition-colors"
              >
                閉じる
              </button>
            </div>
          </div>
        </div>
      </td>
    </tr>
</template>

<script setup lang="ts">
import type { Task } from "~/types/task";
import type { Client } from "~/types/client";
import type { Item } from "~/types/item";

interface Props {
  task: Task;
  isExpanded: boolean;
  clients: Client[];
  items: Item[];
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "toggleExpanded", taskId: number): void;
  (e: "update", taskId: number, data: any): void;
  (e: "delete", taskId: number): void;
  (e: "complete", taskId: number): void;
  (e: "revert", taskId: number): void;
  (e: "updateStatus", taskId: number, status: string): void;
}>();

const isEditMode = ref(false);
const showCompleteDialog = ref(false);
const showSuccessDialog = ref(false);
const confirmChecked = ref(false);

const localStatus = ref(props.task.taskStatus);

// propsが更新されたらlocalStatusも更新
watch(() => props.task.taskStatus, (newStatus) => {
  localStatus.value = newStatus;
});

const formData = ref<any>({
  title: "",
  status: "TODO",
  requestDate: "",
  expectedDeliveryDate: "",
  deliveryDate: null,
  clientId: null,
  note: "",
  items: []
});

const toggleExpanded = () => {
  emit("toggleExpanded", props.task.taskId);
};

const startEdit = () => {
  isEditMode.value = true;
  formData.value = {
    title: props.task.taskTitle.value,
    status: props.task.taskStatus,
    requestDate: props.task.requestDate || "",
    expectedDeliveryDate: props.task.expectedDeliveryDate || "",
    deliveryDate: props.task.deliveryDate || null,
    clientId: props.task.client?.clientId || null,
    note: props.task.note || "",
    items: props.task.taskItems?.map(ti => ({
      itemId: ti.item.itemId,
      quantity: ti.quantity,
      amount: ti.amount
    })) || []
  };
  if (formData.value.items.length === 0) {
    formData.value.items.push({ itemId: null, quantity: 1, amount: 0 });
  }
};

const cancelEdit = () => {
  isEditMode.value = false;
};

const saveEdit = () => {
  const selectedClient = props.clients.find(c => c.clientId === formData.value.clientId);

  const taskItems = formData.value.items
    .filter((item: any) => item.itemId !== null)
    .map((item: any) => {
      const selectedItem = props.items.find(i => i.itemId === item.itemId);
      return {
        taskItemId: null,
        item: {
          itemId: { value: selectedItem!.itemId },
          itemName: { value: selectedItem!.itemName },
          unitPrice: { value: selectedItem!.unitPrice }
        },
        quantity: { value: item.quantity },
        amount: { value: item.amount }
      };
    });

  const task = {
    taskId: { value: props.task.taskId },
    taskTitle: { value: formData.value.title },
    taskStatus: formData.value.status,
    requestDate: formData.value.requestDate,
    client: selectedClient ? {
      clientId: { value: selectedClient.clientId },
      clientName: { value: selectedClient.clientName },
      clientAbbreviation: { value: selectedClient.clientAbbreviation },
      company: {
        companyId: { value: selectedClient.company.companyId },
        companyName: { value: selectedClient.company.companyName },
        withholdingTax: selectedClient.company.withholdingTax
      }
    } : null,
    note: formData.value.note || null,
    taskItems: taskItems,
    expectedDeliveryDate: formData.value.expectedDeliveryDate,
    deliveryDate: formData.value.deliveryDate
  };

  emit("update", props.task.taskId, task);
  isEditMode.value = false;
};

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

const calculateTotal = (taskItems: any[]) => {
  return taskItems.reduce((sum, item) => sum + item.amount, 0);
};

const closeCompleteDialog = () => {
  showCompleteDialog.value = false;
  confirmChecked.value = false;
};

const handleComplete = async () => {
  emit("complete", props.task.taskId);
  showCompleteDialog.value = false;
  confirmChecked.value = false;

  // 少し待ってから成功ダイアログを表示
  await new Promise(resolve => setTimeout(resolve, 300));
  showSuccessDialog.value = true;
};

const handleStatusChange = () => {
  emit("updateStatus", props.task.taskId, localStatus.value);
};
</script>
