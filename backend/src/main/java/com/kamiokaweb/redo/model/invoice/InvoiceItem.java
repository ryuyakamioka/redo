package com.kamiokaweb.redo.model.invoice;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

public record InvoiceItem(
    Long id,
    Long invoiceId,
    @Schema(description = "摘要") String description,
    @Schema(description = "数量", example = "10") Long quantity,
    @Schema(description = "単価", example = "450") BigDecimal unitPrice,
    @Schema(description = "税率", example = "10") Long totalPrice
) {
    
}