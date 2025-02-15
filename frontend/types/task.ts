export interface TaskId {
  value: number;
}

export interface TaskTitle {
  value: string;
}

export interface Task {
  taskId: TaskId;
  taskTitle: TaskTitle;
  taskStatus: "TODO" | "IN_PROGRESS" | "DONE";
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
