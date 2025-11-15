package com.kamiokaweb.redo.model.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "依頼人略称")
public record ClientAbbreviation(@JsonCreator @JsonValue String value) {
}
