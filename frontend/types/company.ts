export interface CompanyId {
  value: number;
}

export interface CompanyName {
  value: string;
}

export interface Company {
  companyId: number;  // @JsonValueのため直接数値
  companyName: string;  // @JsonValueのため直接文字列
  withholdingTax: boolean;
  freeePartnerId: number | null;
}

export interface CompanyListResponse {
  companyList: Company[];
}
