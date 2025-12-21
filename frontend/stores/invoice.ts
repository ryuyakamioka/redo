import { defineStore } from "pinia";
import { InvoiceEstimate, InvoiceEstimateResponse } from "~/types/invoice";
import { useApi } from "~/utils/api";

export const useInvoiceStore = defineStore("invoice", {
  state: () => ({
    estimates: [] as InvoiceEstimate[],
  }),
  actions: {
    async fetchEstimates(billingMonth: string, companyId?: number, onlyUnbilled?: boolean) {
      try {
        const params: Record<string, string | number | boolean> = {
          billingMonth,
        };
        if (companyId !== undefined) {
          params.companyId = companyId;
        }
        if (onlyUnbilled !== undefined) {
          params.onlyUnbilled = onlyUnbilled;
        }

        const response = await useApi().get<InvoiceEstimateResponse>(
          "/invoices/estimate",
          { params }
        );
        this.estimates = response.data.estimates;
      } catch (error) {
        console.error("請求予定の取得エラー:", error);
        throw error;
      }
    },
  },
});
