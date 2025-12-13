export interface InvoiceEstimateItem {
  description: string;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
}

export interface InvoiceEstimate {
  companyId: number;
  year: number;
  month: number;
  subtotal: number;
  items: InvoiceEstimateItem[];
}

export interface InvoiceEstimateResponse {
  estimates: InvoiceEstimate[];
}
