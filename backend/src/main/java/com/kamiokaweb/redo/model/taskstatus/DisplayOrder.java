package com.kamiokaweb.redo.model.taskstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public record DisplayOrder(@JsonCreator(mode = JsonCreator.Mode.PROPERTIES) @JsonProperty("value") @JsonValue Integer value) {
}
