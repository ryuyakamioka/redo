<template>
  <div class="flex flex-col h-full">
    <!-- ページヘッダー -->
    <PageHeader title="依頼管理">
      <template #actions>
      </template>
    </PageHeader>

    <!-- メインコンテンツ -->
    <div class="flex-1 overflow-auto bg-gray-50">
      <div class="p-6">
        <!-- 依頼登録フォーム -->
        <TaskForm :clients="clients" :items="items" @submit="createTask" />

        <!-- ステータスフィルター -->
        <div class="mb-4 bg-white rounded-lg shadow border border-gray-200 p-4">
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-4">
              <label class="text-sm font-medium text-gray-700">絞り込み:</label>
              <select
                v-model="statusFilter"
                class="px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white text-sm"
              >
                <option value="ALL">完了を含む</option>
                <option value="INCOMPLETE">完了を含まない</option>
                <option value="DONE_ONLY">完了のみ</option>
              </select>
            </div>
            <div class="text-sm text-gray-600">
              表示中の依頼: <span class="font-medium text-gray-700">{{ summary.count }}件</span> <span class="font-medium text-gray-700">¥{{ summary.total.toLocaleString() }}</span>
            </div>
          </div>
        </div>

        <!-- 依頼テーブル -->
        <TaskList :tasks="filteredTasks" :clients="clients" :items="items" @update="updateTask" @delete="deleteTask" @complete="completeTask" @revert="revertTask" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useTaskStore } from "~/stores/task";
import { useClientStore } from "~/stores/client";
import { useItemStore } from "~/stores/item";
import { computed } from "vue";
import PageHeader from "~/components/layout/PageHeader.vue";
import TaskForm from "~/components/task/TaskForm.vue";
import TaskList from "~/components/task/TaskList.vue";

const taskStore = useTaskStore();
const clientStore = useClientStore();
const itemStore = useItemStore();

// マスタデータと依頼データを取得
await Promise.all([
  taskStore.fetchTasks(),
  clientStore.fetchClients(),
  itemStore.fetchItems()
]);

const clients = computed(() => clientStore.clients);
const items = computed(() => itemStore.items);
const tasks = computed(() => taskStore.tasks);

// ステータスフィルター
const statusFilter = ref<'ALL' | 'INCOMPLETE' | 'DONE_ONLY'>('INCOMPLETE');

// フィルター済みの依頼リスト
const filteredTasks = computed(() => {
  if (statusFilter.value === 'ALL') {
    return tasks.value;
  } else if (statusFilter.value === 'DONE_ONLY') {
    // 完了のみ
    return tasks.value.filter(task => task.taskStatus === 'DONE');
  }
  // 完了を含まない（未完了のみ）
  return tasks.value.filter(task => task.taskStatus !== 'DONE');
});

// 表示中の依頼のサマリー
const summary = computed(() => {
  const count = filteredTasks.value.length;
  const total = filteredTasks.value.reduce((sum, task) => {
    return sum + task.taskItems.reduce((itemSum, item) => itemSum + item.amount, 0);
  }, 0);
  return { count, total };
});

const createTask = async (formData: any) => {
  // 選択された依頼人を取得
  const selectedClient = clients.value.find(c => c.clientId === formData.clientId);

  // 依頼明細を構築
  const taskItems = formData.items
    .filter((item: any) => item.itemId !== null)
    .map((item: any) => {
      const selectedItem = items.value.find(i => i.itemId === item.itemId);
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
    taskId: { value: 0 },
    taskTitle: { value: formData.title },
    taskStatus: formData.status,
    requestDate: formData.requestDate,
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
    note: formData.note || null,
    taskItems: taskItems,
    expectedDeliveryDate: formData.expectedDeliveryDate,
    deliveryDate: formData.deliveryDate
  };

  await taskStore.createTask(task);
};

const updateTask = async (taskId: number, task: any) => {
  try {
    await taskStore.updateTask(taskId, task);
  } catch (error) {
    console.error("依頼の更新エラー:", error);
    alert("依頼の更新に失敗しました");
  }
};

const deleteTask = async (taskId: number) => {
  if (confirm('この依頼を削除してもよろしいですか？')) {
    await taskStore.deleteTask(taskId);
  }
};

const completeTask = async (taskId: number) => {
  try {
    await taskStore.completeTask(taskId);
  } catch (error) {
    console.error("依頼の完了エラー:", error);
    alert("依頼の完了に失敗しました");
  }
};

const revertTask = async (taskId: number) => {
  if (confirm('この依頼を作業中に戻してもよろしいですか？')) {
    try {
      await taskStore.revertTask(taskId);
    } catch (error) {
      console.error("依頼の復帰エラー:", error);
      alert("依頼の復帰に失敗しました");
    }
  }
};
</script>
