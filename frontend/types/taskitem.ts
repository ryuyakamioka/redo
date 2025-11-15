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
  taskItemId: number | null;
  item: Item;
  quantity: Quantity;
  amount: Amount;
}
