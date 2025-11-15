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
  itemId: number;  // @JsonValueのため直接数値
  itemName: string;  // @JsonValueのため直接文字列
  unitPrice: number;  // @JsonValueのため直接数値
}

export interface ItemListResponse {
  itemList: Item[];
}
