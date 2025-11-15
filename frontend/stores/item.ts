import { defineStore } from "pinia";
import { Item, ItemListResponse } from "~/types/item";
import { useApi } from "~/utils/api";

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
  },
});
