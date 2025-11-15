import { defineStore } from "pinia";
import { Client, ClientListResponse } from "~/types/client";
import { useApi } from "~/utils/api";

export const useClientStore = defineStore("client", {
  state: () => ({
    clients: [] as Client[],
  }),
  actions: {
    async fetchClients() {
      try {
        const response = await useApi().get<ClientListResponse>("/clients");
        this.clients = response.data.clientList;
      } catch (error) {
        console.error("依頼人の取得エラー:", error);
      }
    },
  },
});
