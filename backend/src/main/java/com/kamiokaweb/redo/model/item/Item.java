package com.kamiokaweb.redo.model.item;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "品目")
public record Item(
        ItemId itemId,
        ItemName itemName,
        UnitPrice unitPrice
) {
}
