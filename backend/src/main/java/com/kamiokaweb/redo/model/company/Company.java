package com.kamiokaweb.redo.model.company;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "会社")
public record Company(
        CompanyId companyId,
        CompanyName companyName,
        @Schema(description = "源泉徴収有無") Boolean withholdingTax
) {
}
