package com.kamiokaweb.redo.model.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "品目名")
public record ItemName(@JsonCreator(mode = JsonCreator.Mode.PROPERTIES) @JsonProperty("value") @JsonValue String value) {
}
