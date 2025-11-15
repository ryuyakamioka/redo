import { Client } from "./client";
import { TaskItem } from "./taskitem";

export interface TaskId {
  value: number;
}

export interface TaskTitle {
  value: string;
}

export interface Task {
  taskId: number;  // @JsonValueのため直接数値
  taskTitle: TaskTitle;
  taskStatus: "TODO" | "IN_PROGRESS" | "DONE";
  requestDate: string | null;  // ISO 8601 date string
  client: Client | null;
  note: string | null;
  taskItems: TaskItem[];
  expectedDeliveryDate: string | null;  // ISO 8601 date string
  deliveryDate: string | null;  // ISO 8601 date string
}

export interface TaskRequest {
  task: Task;
}

export interface TaskListResponse {
  taskList: Task[];
}

export interface TaskResponse {
  task: Task;
}
