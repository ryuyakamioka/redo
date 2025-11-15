package com.kamiokaweb.redo.repository.item;

import com.kamiokaweb.redo.infrastructure.item.ItemAccessor;
import com.kamiokaweb.redo.infrastructure.item.ItemDto;
import com.kamiokaweb.redo.model.item.Item;
import com.kamiokaweb.redo.model.item.ItemId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final ItemAccessor itemAccessor;

    public ItemRepositoryImpl(ItemAccessor itemAccessor) {
        this.itemAccessor = itemAccessor;
    }

    @Override
    public Optional<Item> get(ItemId itemId) {
        return itemAccessor.findById(itemId.value())
                .map(ItemDto::from);
    }

    @Override
    public List<Item> getList() {
        return StreamSupport.stream(itemAccessor.findAll().spliterator(), false)
                .map(ItemDto::from)
                .toList();
    }

    @Override
    public Item save(Item item) {
        ItemDto dto = new ItemDto(
                item.itemId() != null ? item.itemId().value() : null,
                item.itemName().value(),
                item.unitPrice().value(),
                null
        );
        ItemDto saved = itemAccessor.save(dto);
        return saved.from();
    }

    @Override
    public void delete(ItemId itemId) {
        itemAccessor.deleteById(itemId.value());
    }
}
