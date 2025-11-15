export interface ItemId {
  value: number;
}

export interface ItemName {
  value: string;
}

export interface UnitPrice {
  value: number;
}

export interface Item {
  itemId: number;
  itemName: ItemName;
  unitPrice: UnitPrice;
}

export interface ItemListResponse {
  itemList: Item[];
}
