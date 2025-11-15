export interface CompanyId {
  value: number;
}

export interface CompanyName {
  value: string;
}

export interface Company {
  companyId: number;
  companyName: CompanyName;
  withholdingTax: boolean;
}

export interface CompanyListResponse {
  companyList: Company[];
}
