package com.kamiokaweb.redo.model.taskitem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "件数")
public record Quantity(@JsonCreator @JsonValue Integer value) {
}
