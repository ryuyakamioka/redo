package com.kamiokaweb.redo.model.taskitem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "受注金額")
public record Amount(@JsonCreator @JsonValue BigDecimal value) {
}
