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
      <TaskForm :clients="clients" :items="items" @submit="createTask" />

      <!-- 依頼テーブル -->
      <TaskList :tasks="tasks" @delete="deleteTask" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { useTaskStore } from "~/stores/task";
import { useClientStore } from "~/stores/client";
import { useItemStore } from "~/stores/item";
import { computed } from "vue";
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

const deleteTask = async (taskId: number) => {
  if (confirm('この依頼を削除してもよろしいですか？')) {
    await taskStore.deleteTask(taskId);
  }
};
</script>
