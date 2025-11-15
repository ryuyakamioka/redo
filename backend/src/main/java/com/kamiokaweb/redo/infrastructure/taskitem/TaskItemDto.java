package com.kamiokaweb.redo.infrastructure.taskitem;

import com.kamiokaweb.redo.model.item.Item;
import com.kamiokaweb.redo.model.taskitem.Amount;
import com.kamiokaweb.redo.model.taskitem.Quantity;
import com.kamiokaweb.redo.model.taskitem.TaskItem;
import com.kamiokaweb.redo.model.taskitem.TaskItemId;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("task_items")
public record TaskItemDto(
        @Id
        Long id,
        Long taskId,
        Long itemId,
        Integer quantity,
        BigDecimal amount,
        LocalDateTime createdAt
) {
    public TaskItemDto(Long taskId, TaskItem taskItem) {
        this(
                taskItem.taskItemId() != null ? taskItem.taskItemId().value() : null,
                taskId,
                taskItem.item().itemId().value(),
                taskItem.quantity().value(),
                taskItem.amount().value(),
                LocalDateTime.now()
        );
    }

    public TaskItem from(Item item) {
        return new TaskItem(
                new TaskItemId(id),
                item,
                new Quantity(quantity),
                new Amount(amount)
        );
    }
}
