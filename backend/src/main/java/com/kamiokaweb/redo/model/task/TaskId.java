package com.kamiokaweb.redo.model.task;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "依頼のID")
public record TaskId(@JsonValue Long value) {
}
