package com.kamiokaweb.redo.model.invoice;

import java.time.Month;
import java.time.Year;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "計上年月")
public record AccountingPeriod(
    Year year,
    Month month
) {
}
