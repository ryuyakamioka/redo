package com.kamiokaweb.redo.infrastructure.item;

import com.kamiokaweb.redo.model.item.Item;
import com.kamiokaweb.redo.model.item.ItemId;
import com.kamiokaweb.redo.model.item.ItemName;
import com.kamiokaweb.redo.model.item.UnitPrice;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("items")
public record ItemDto(
        @Id
        Long id,
        String name,
        BigDecimal unitPrice,
        LocalDateTime createdAt
) {
    public Item from() {
        return new Item(
                new ItemId(id),
                new ItemName(name),
                new UnitPrice(unitPrice)
        );
    }
}
