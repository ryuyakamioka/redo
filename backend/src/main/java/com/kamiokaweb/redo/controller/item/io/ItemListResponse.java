package com.kamiokaweb.redo.controller.item.io;

import com.kamiokaweb.redo.model.item.Item;

import java.util.List;

public record ItemListResponse(
        List<Item> itemList
) {
    public static ItemListResponse of(List<Item> items) {
        return new ItemListResponse(items);
    }
}
