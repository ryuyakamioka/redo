import { defineStore } from "pinia";
import { ref } from "vue";
import type { TaskStatusMaster, TaskStatusListResponse } from "~/types/taskstatus";
import { useApi } from "~/utils/api";

interface TaskStatusResponse {
  taskStatus: TaskStatusMaster;
}

export const useTaskStatusStore = defineStore("taskstatus", () => {
  const taskStatuses = ref<TaskStatusMaster[]>([]);
  const taskStatus = ref<TaskStatusMaster | null>(null);

  const fetchTaskStatuses = async () => {
    try {
      const response = await useApi().get<TaskStatusListResponse>("/task-statuses");
      taskStatuses.value = response.data.taskStatusList;
    } catch (error) {
      console.error("依頼ステータス一覧の取得エラー:", error);
    }
  };

  const createTaskStatus = async (taskStatusData: any) => {
    try {
      const taskStatusRequest = { taskStatus: taskStatusData };
      const response = await useApi().post<TaskStatusResponse>("/task-status", taskStatusRequest);
      if (response.status === 200) {
        await fetchTaskStatuses();
      }
    } catch (error) {
      console.error("依頼ステータスの作成エラー:", error);
      throw error;
    }
  };

  const updateTaskStatus = async (id: number, taskStatusData: any) => {
    try {
      const taskStatusRequest = { taskStatus: taskStatusData };
      const response = await useApi().put<TaskStatusResponse>(`/task-status/${id}`, taskStatusRequest);
      if (response.status === 200) {
        await fetchTaskStatuses();
      }
    } catch (error) {
      console.error("依頼ステータスの更新エラー:", error);
      throw error;
    }
  };

  const deleteTaskStatus = async (id: number) => {
    try {
      const response = await useApi().delete(`/task-status/${id}`);
      if (response.status === 200) {
        await fetchTaskStatuses();
      }
    } catch (error) {
      console.error("依頼ステータスの削除エラー:", error);
      throw error;
    }
  };

  const getByCode = (code: string): TaskStatusMaster | undefined => {
    return taskStatuses.value.find(status => status.taskStatusCode === code);
  };

  return {
    taskStatuses,
    taskStatus,
    fetchTaskStatuses,
    createTaskStatus,
    updateTaskStatus,
    deleteTaskStatus,
    getByCode,
  };
});
