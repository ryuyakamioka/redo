package com.kamiokaweb.redo.controller.item;

import com.kamiokaweb.redo.controller.item.io.ItemListResponse;
import com.kamiokaweb.redo.controller.item.io.ItemRequest;
import com.kamiokaweb.redo.controller.item.io.ItemResponse;
import com.kamiokaweb.redo.model.item.Item;
import com.kamiokaweb.redo.usecase.ItemUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ItemController {
    private final ItemUseCase itemUseCase;

    public ItemController(ItemUseCase itemUseCase) {
        this.itemUseCase = itemUseCase;
    }

    @GetMapping("/items")
    public ItemListResponse getItems() {
        return ItemListResponse.of(itemUseCase.getList());
    }

    @PostMapping("/item")
    public ResponseEntity<ItemResponse> createItem(@RequestBody ItemRequest request) {
        Item created = itemUseCase.create(request.item());
        return ResponseEntity.ok(ItemResponse.of(created));
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable Long id, @RequestBody ItemRequest request) {
        Item updated = itemUseCase.update(request.item());
        return ResponseEntity.ok(ItemResponse.of(updated));
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemUseCase.delete(id);
        return ResponseEntity.ok().build();
    }
}
