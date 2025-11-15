package com.kamiokaweb.redo.controller.item;

import com.kamiokaweb.redo.controller.item.io.ItemListResponse;
import com.kamiokaweb.redo.usecase.ItemUseCase;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://15.168.89.85"})
public class ItemController {
    private final ItemUseCase itemUseCase;

    public ItemController(ItemUseCase itemUseCase) {
        this.itemUseCase = itemUseCase;
    }

    @GetMapping("/items")
    public ItemListResponse getItems() {
        return ItemListResponse.of(itemUseCase.getList());
    }
}
