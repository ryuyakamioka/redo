package com.kamiokaweb.redo.model.invoice;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "請求予定明細")
public record InvoiceEstimateItem(
    @Schema(description = "摘要") String description,
    @Schema(description = "数量") Long quantity,
    @Schema(description = "単価") BigDecimal unitPrice,
    @Schema(description = "合計金額") BigDecimal totalPrice
) {
}
