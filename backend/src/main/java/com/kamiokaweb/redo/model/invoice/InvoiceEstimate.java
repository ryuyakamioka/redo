package com.kamiokaweb.redo.model.invoice;

import java.math.BigDecimal;
import java.util.List;

import com.kamiokaweb.redo.model.company.CompanyId;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "請求予定")
public record InvoiceEstimate(
    CompanyId companyId,
    AccountingPeriod accountingPeriod,
    @Schema(description = "小計") BigDecimal subtotal,
    List<InvoiceEstimateItem> items
) {
}
