package com.kamiokaweb.redo.model.taskitem;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "依頼明細のID")
public record TaskItemId(@JsonValue Long value) {
}
