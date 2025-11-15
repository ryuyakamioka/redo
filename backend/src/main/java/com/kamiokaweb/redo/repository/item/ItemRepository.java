package com.kamiokaweb.redo.repository.item;

import com.kamiokaweb.redo.model.item.Item;
import com.kamiokaweb.redo.model.item.ItemId;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Optional<Item> get(ItemId itemId);
    List<Item> getList();
    Item save(Item item);
    void delete(ItemId itemId);
}
