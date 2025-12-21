package com.kamiokaweb.redo.model.client;

import com.kamiokaweb.redo.model.company.Company;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "依頼人")
public record Client(
        ClientId clientId,
        ClientName clientName,
        ClientAbbreviation clientAbbreviation,
        @Schema(description = "会社") Company company,
        @Schema(description = "摘要に依頼人名を表示") Boolean showClientNameInDescription
) {
}
