import { defineStore } from "pinia";
import { Client, ClientListResponse } from "~/types/client";
import { useApi } from "~/utils/api";

interface ClientResponse {
  client: Client;
}

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
    async createClient(clientData: any) {
      try {
        const clientRequest = { client: clientData };
        const response = await useApi().post<ClientResponse>("/client", clientRequest);
        if (response.status === 200) {
          await this.fetchClients();
        }
      } catch (error) {
        console.error("依頼人の作成エラー:", error);
        throw error;
      }
    },
    async updateClient(id: number, clientData: any) {
      try {
        const clientRequest = { client: clientData };
        const response = await useApi().put<ClientResponse>(`/client/${id}`, clientRequest);
        if (response.status === 200) {
          await this.fetchClients();
        }
      } catch (error) {
        console.error("依頼人の更新エラー:", error);
        throw error;
      }
    },
    async deleteClient(id: number) {
      try {
        const response = await useApi().delete(`/client/${id}`);
        if (response.status === 200) {
          await this.fetchClients();
        }
      } catch (error) {
        console.error("依頼人の削除エラー:", error);
        throw error;
      }
    },
  },
});
