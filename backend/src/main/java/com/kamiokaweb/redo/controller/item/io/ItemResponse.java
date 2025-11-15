package com.kamiokaweb.redo.controller.item.io;

import com.kamiokaweb.redo.model.item.Item;

public record ItemResponse(
        Item item
) {
    public static ItemResponse of(Item item) {
        return new ItemResponse(item);
    }
}
