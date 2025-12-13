package com.kamiokaweb.redo.model.invoice.amount;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public record WithholdingTaxAmount(@JsonCreator(mode = JsonCreator.Mode.PROPERTIES) @JsonProperty("value") @JsonValue BigDecimal value) {

}
