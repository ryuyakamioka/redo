package com.kamiokaweb.redo.model.invoice.amount;

public record InvoiceAmountSummary(
    SubtotalAmount subtotal,
    ConsumptionTaxAmount consumptionTax,
    WithholdingTaxAmount withholdingTax,
    TotalInvoiceAmount totalAmount
) {

}
