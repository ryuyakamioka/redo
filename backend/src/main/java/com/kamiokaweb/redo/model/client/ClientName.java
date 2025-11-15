package com.kamiokaweb.redo.model.client;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "依頼人名")
public record ClientName(@JsonValue String value) {
}
