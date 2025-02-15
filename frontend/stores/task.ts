import { defineStore } from "pinia";
import axios from "axios";
import { useRuntimeConfig } from "#app";
import {
  Task,
  TaskListResponse,
  TaskResponse,
  TaskRequest,
} from "~/types/task";

export const useApi = () => {
  const config = useRuntimeConfig();
  return axios.create({
    baseURL: config.public.apiBase,
  });
};

export const useTaskStore = defineStore("task", {
  state: () => ({
    tasks: [] as Task[],
    task: null as Task | null,
  }),
  actions: {
    async fetchTasks() {
        try {
          const api = useApi();
          const response = await api.get<TaskListResponse>("/tasks");
          this.tasks = response.data.taskList;
        } catch (error) {
          console.error("タスクの取得エラー:", error);
        }
      },
    async fetchTask(taskId: number) {
      try {
        const response = await axios.get<TaskResponse>(
          `task/${taskId}`
        );
        this.task = response.data.task;
      } catch (error) {
        console.error("タスクの取得エラー:", error);
      }
    },
    async createTask(task: Task) {
      try {
        const taskRequest: TaskRequest = { task };
        const response = await axios.post(
          "/task",
          taskRequest
        );
        if (response.status === 200) {
          await this.fetchTasks();
        }
      } catch (error) {
        console.error("タスクの作成エラー:", error);
      }
    },
  },
});
