package com.kamiokaweb.redo.model.taskitem;

import com.kamiokaweb.redo.model.item.Item;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "依頼明細")
public record TaskItem(
        TaskItemId taskItemId,
        @Schema(description = "品目") Item item,
        Quantity quantity,
        Amount amount
) {
}
