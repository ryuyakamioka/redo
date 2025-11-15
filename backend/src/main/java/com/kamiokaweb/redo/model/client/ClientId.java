package com.kamiokaweb.redo.model.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "依頼人のID")
public record ClientId(@JsonCreator @JsonValue Long value) {
}
