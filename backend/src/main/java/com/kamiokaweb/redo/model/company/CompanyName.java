package com.kamiokaweb.redo.model.company;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "会社名")
public record CompanyName(@JsonCreator @JsonValue String value) {
}
