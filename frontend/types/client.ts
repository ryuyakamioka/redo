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
  clientId: number;
  clientName: ClientName;
  clientAbbreviation: ClientAbbreviation;
  company: Company;
}

export interface ClientListResponse {
  clientList: Client[];
}
