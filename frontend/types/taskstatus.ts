export interface TaskStatusMaster {
  taskStatusId: number;  // @JsonValueのため直接数値
  taskStatusCode: string;  // @JsonValueのため直接文字列
  taskStatusName: string;  // @JsonValueのため直接文字列
  displayColor: string;  // @JsonValueのため直接文字列
  displayOrder: number;  // @JsonValueのため直接数値
  isCompleted: boolean;
}

export interface TaskStatusListResponse {
  taskStatusList: TaskStatusMaster[];
}
