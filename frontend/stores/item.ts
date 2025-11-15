import { defineStore } from "pinia";
import { Item, ItemListResponse } from "~/types/item";
import { useApi } from "~/utils/api";

interface ItemResponse {
  item: Item;
}

export const useItemStore = defineStore("item", {
  state: () => ({
    items: [] as Item[],
  }),
  actions: {
    async fetchItems() {
      try {
        const response = await useApi().get<ItemListResponse>("/items");
        this.items = response.data.itemList;
      } catch (error) {
        console.error("品目の取得エラー:", error);
      }
    },
    async createItem(itemData: any) {
      try {
        const itemRequest = { item: itemData };
        const response = await useApi().post<ItemResponse>("/item", itemRequest);
        if (response.status === 200) {
          await this.fetchItems();
        }
      } catch (error) {
        console.error("品目の作成エラー:", error);
        throw error;
      }
    },
    async updateItem(id: number, itemData: any) {
      try {
        const itemRequest = { item: itemData };
        const response = await useApi().put<ItemResponse>(`/item/${id}`, itemRequest);
        if (response.status === 200) {
          await this.fetchItems();
        }
      } catch (error) {
        console.error("品目の更新エラー:", error);
        throw error;
      }
    },
    async deleteItem(id: number) {
      try {
        const response = await useApi().delete(`/item/${id}`);
        if (response.status === 200) {
          await this.fetchItems();
        }
      } catch (error) {
        console.error("品目の削除エラー:", error);
        throw error;
      }
    },
  },
});
