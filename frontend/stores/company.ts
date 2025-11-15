import { defineStore } from "pinia";
import { ref } from "vue";
import type { Company, CompanyListResponse } from "~/types/company";
import { useApi } from "~/utils/api";

interface CompanyResponse {
  company: Company;
}

export const useCompanyStore = defineStore("company", () => {
  const companies = ref<Company[]>([]);
  const company = ref<Company | null>(null);

  const fetchCompanies = async () => {
    try {
      const response = await useApi().get<CompanyListResponse>("/companies");
      companies.value = response.data.companyList;
    } catch (error) {
      console.error("会社一覧の取得エラー:", error);
    }
  };

  const createCompany = async (companyData: any) => {
    try {
      const companyRequest = { company: companyData };
      const response = await useApi().post<CompanyResponse>("/company", companyRequest);
      if (response.status === 200) {
        await fetchCompanies();
      }
    } catch (error) {
      console.error("会社の作成エラー:", error);
      throw error;
    }
  };

  const updateCompany = async (id: number, companyData: any) => {
    try {
      const companyRequest = { company: companyData };
      const response = await useApi().put<CompanyResponse>(`/company/${id}`, companyRequest);
      if (response.status === 200) {
        await fetchCompanies();
      }
    } catch (error) {
      console.error("会社の更新エラー:", error);
      throw error;
    }
  };

  const deleteCompany = async (id: number) => {
    try {
      const response = await useApi().delete(`/company/${id}`);
      if (response.status === 200) {
        await fetchCompanies();
      }
    } catch (error) {
      console.error("会社の削除エラー:", error);
      throw error;
    }
  };

  return {
    companies,
    company,
    fetchCompanies,
    createCompany,
    updateCompany,
    deleteCompany,
  };
});
