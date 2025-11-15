package com.kamiokaweb.redo.model.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "単価")
public record UnitPrice(@JsonCreator @JsonValue BigDecimal value) {
}
