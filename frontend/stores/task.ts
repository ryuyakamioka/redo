import { defineStore } from "pinia";
import {
  Task,
  TaskListResponse,
  TaskResponse,
  TaskRequest,
} from "~/types/task";
import { useApi } from "~/utils/api";

export const useTaskStore = defineStore("task", {
  state: () => ({
    tasks: [] as Task[],
    task: null as Task | null,
  }),
  actions: {
    async fetchTasks() {
      try {
        const response = await useApi().get<TaskListResponse>("/tasks");
        this.tasks = response.data.taskList;
      } catch (error) {
        console.error("依頼の取得エラー:", error);
      }
    },
    async fetchTask(taskId: number) {
      try {
        const response = await useApi().get<TaskResponse>(`task/${taskId}`);
        this.task = response.data.task;
      } catch (error) {
        console.error("依頼の取得エラー:", error);
      }
    },
    async createTask(task: any) {
      try {
        const taskRequest = { task };
        const response = await useApi().post("/task", taskRequest);
        if (response.status === 200) {
          await this.fetchTasks();
        }
      } catch (error) {
        console.error("依頼の作成エラー:", error);
      }
    },
    async updateTask(taskId: number, task: any) {
      try {
        const taskRequest = { task };
        const response = await useApi().put(`/task/${taskId}`, taskRequest);
        if (response.status === 200) {
          await this.fetchTasks();
        }
      } catch (error) {
        console.error("依頼の更新エラー:", error);
        throw error;
      }
    },
    async deleteTask(taskId: number) {
      try {
        const response = await useApi().delete(`/task/${taskId}`);
        if (response.status === 200) {
          await this.fetchTasks();
        }
      } catch (error) {
        console.error("依頼の削除エラー:", error);
      }
    },
    async completeTask(taskId: number) {
      try {
        const response = await useApi().put(`/task/${taskId}/complete`);
        if (response.status === 200) {
          await this.fetchTasks();
        }
      } catch (error) {
        console.error("依頼の完了エラー:", error);
        throw error;
      }
    },
  },
});
