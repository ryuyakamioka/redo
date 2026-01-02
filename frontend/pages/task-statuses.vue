<template>
  <div class="flex flex-col h-full">
    <PageHeader title="依頼ステータス管理">
      <template #actions>
      </template>
    </PageHeader>

    <div class="flex-1 overflow-auto bg-gray-50">
      <div class="p-6">
        <TaskStatusForm
          :editTaskStatus="editingTaskStatus"
          @submit="handleSubmit"
          @cancel="handleCancelEdit"
        />

        <TaskStatusList
          :taskStatuses="taskStatuses"
          @edit="handleEdit"
          @delete="handleDelete"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useTaskStatusStore } from "~/stores/taskstatus";
import { computed, ref } from "vue";
import PageHeader from "~/components/layout/PageHeader.vue";
import TaskStatusForm from "~/components/taskstatus/TaskStatusForm.vue";
import TaskStatusList from "~/components/taskstatus/TaskStatusList.vue";
import type { TaskStatusMaster } from "~/types/taskstatus";

const taskStatusStore = useTaskStatusStore();

await taskStatusStore.fetchTaskStatuses();

const taskStatuses = computed(() => taskStatusStore.taskStatuses);
const editingTaskStatus = ref<TaskStatusMaster | null>(null);

const handleSubmit = async (formData: any) => {
  try {
    if (editingTaskStatus.value) {
      await taskStatusStore.updateTaskStatus(editingTaskStatus.value.taskStatusId, formData);
      editingTaskStatus.value = null;
    } else {
      await taskStatusStore.createTaskStatus(formData);
    }
  } catch (error) {
    console.error("ステータスの保存エラー:", error);
    alert("ステータスの保存に失敗しました");
  }
};

const handleEdit = (taskStatus: TaskStatusMaster) => {
  editingTaskStatus.value = taskStatus;
  window.scrollTo({ top: 0, behavior: 'smooth' });
};

const handleCancelEdit = () => {
  editingTaskStatus.value = null;
};

const handleDelete = async (taskStatusId: number) => {
  if (confirm('このステータスを削除してもよろしいですか？')) {
    try {
      await taskStatusStore.deleteTaskStatus(taskStatusId);
    } catch (error) {
      console.error("ステータスの削除エラー:", error);
      alert("ステータスの削除に失敗しました");
    }
  }
};
</script>
