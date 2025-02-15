import { defineStore } from "pinia";
import axios from "axios";
import {
  Task,
  TaskListResponse,
  TaskResponse,
  TaskRequest,
} from "~/types/task";

export const useTaskStore = defineStore("task", {
  state: () => ({
    tasks: [] as Task[],
    task: null as Task | null,
  }),
  actions: {
    async fetchTasks() {
      try {
        const response = await axios.get<TaskListResponse>("/tasks", {
          baseURL: "http://15.168.89.85:8080",
        });
        this.tasks = response.data.taskList;
      } catch (error) {
        console.error("タスクの取得エラー:", error);
      }
    },
    async fetchTask(taskId: number) {
      try {
        const response = await axios.get<TaskResponse>(
          `http://15.168.89.85:8080/task/${taskId}`
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
          "http://15.168.89.85:8080/task",
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
