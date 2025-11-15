package com.kamiokaweb.redo.usecase;

import com.kamiokaweb.redo.model.item.Item;
import com.kamiokaweb.redo.repository.item.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemUseCase {
    private final ItemRepository itemRepository;

    public ItemUseCase(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getList() {
        return itemRepository.getList();
    }

    public Item create(Item item) {
        return itemRepository.save(item);
    }

    public Item update(Item item) {
        return itemRepository.save(item);
    }

    public void delete(Long itemId) {
        itemRepository.delete(new com.kamiokaweb.redo.model.item.ItemId(itemId));
    }
}
