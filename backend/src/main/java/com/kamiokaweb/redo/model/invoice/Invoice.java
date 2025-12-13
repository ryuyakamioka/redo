package com.kamiokaweb.redo.model.invoice;

import java.time.LocalDate;
import java.util.List;

import com.kamiokaweb.redo.model.company.CompanyId;
import com.kamiokaweb.redo.model.invoice.amount.InvoiceAmountSummary;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "請求書")
public record Invoice(
    InvoiceId invoiceId,
    CompanyId companyId,
    AccountingPeriod accountingPeriod,
    @Schema(description = "請求書発行日") LocalDate issueDate,
    InvoiceAmountSummary amountSummary,
    List<InvoiceItem> items
) {
}
