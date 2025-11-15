import { Company } from "./company";

export interface ClientId {
  value: number;
}

export interface ClientName {
  value: string;
}

export interface ClientAbbreviation {
  value: string;
}

export interface Client {
  clientId: number;  // @JsonValueのため直接数値
  clientName: string;  // @JsonValueのため直接文字列
  clientAbbreviation: string;  // @JsonValueのため直接文字列
  company: Company;
}

export interface ClientListResponse {
  clientList: Client[];
}
