package com.kamiokaweb.redo.controller.invoice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kamiokaweb.redo.controller.invoice.io.InvoiceEstimateResponse;
import com.kamiokaweb.redo.usecase.InvoiceEstimateUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "請求書", description = "請求書関連API")
public class InvoiceController {

    private final InvoiceEstimateUseCase invoiceEstimateUseCase;

    public InvoiceController(InvoiceEstimateUseCase invoiceEstimateUseCase) {
        this.invoiceEstimateUseCase = invoiceEstimateUseCase;
    }

    @GetMapping("/invoices/estimate")
    @Operation(summary = "請求予定取得", description = "指定した条件で請求対象の依頼データを集計し、請求予定金額を返す")
    public InvoiceEstimateResponse getInvoiceEstimate(
        @Parameter(description = "請求予定年月（形式: 202512）", required = true)
        @RequestParam String billingMonth,
        @Parameter(description = "請求先会社ID（任意、未指定の場合は全会社）")
        @RequestParam(required = false) Long companyId,
        @Parameter(description = "未請求のみ取得（任意、デフォルトfalse）")
        @RequestParam(required = false) Boolean onlyUnbilled
    ) {
        var estimates = invoiceEstimateUseCase.getEstimates(billingMonth, companyId, onlyUnbilled);
        return InvoiceEstimateResponse.from(estimates);
    }
}
