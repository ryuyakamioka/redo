import { Item } from "./item";

export interface TaskItemId {
  value: number;
}

export interface Quantity {
  value: number;
}

export interface Amount {
  value: number;
}

export interface TaskItem {
  taskItemId: number | null;  // @JsonValueのため直接数値
  item: Item;
  quantity: number;  // @JsonValueのため直接数値
  amount: number;  // @JsonValueのため直接数値
}
