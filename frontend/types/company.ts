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
}

export interface CompanyListResponse {
  companyList: Company[];
}
