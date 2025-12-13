package com.kamiokaweb.redo.controller.invoice.io;

import java.math.BigDecimal;
import java.util.List;

import com.kamiokaweb.redo.model.invoice.InvoiceEstimate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "請求予定レスポンス")
public record InvoiceEstimateResponse(
    List<InvoiceEstimateData> estimates
) {
    public static InvoiceEstimateResponse from(List<InvoiceEstimate> estimates) {
        return new InvoiceEstimateResponse(
            estimates.stream()
                .map(InvoiceEstimateData::from)
                .toList()
        );
    }

    @Schema(description = "請求予定データ")
    public record InvoiceEstimateData(
        Long companyId,
        Integer year,
        Integer month,
        BigDecimal subtotal,
        List<InvoiceEstimateItemData> items
    ) {
        public static InvoiceEstimateData from(InvoiceEstimate estimate) {
            return new InvoiceEstimateData(
                estimate.companyId().value(),
                estimate.accountingPeriod().year().getValue(),
                estimate.accountingPeriod().month().getValue(),
                estimate.subtotal(),
                estimate.items().stream()
                    .map(item -> new InvoiceEstimateItemData(
                        item.description(),
                        item.quantity(),
                        item.unitPrice(),
                        item.totalPrice()
                    ))
                    .toList()
            );
        }
    }

    @Schema(description = "請求予定明細データ")
    public record InvoiceEstimateItemData(
        String description,
        Long quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
    ) {
    }
}
