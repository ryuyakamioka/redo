package com.kamiokaweb.redo.model.invoice;

import java.math.BigDecimal;

import com.kamiokaweb.redo.model.client.Client;
import com.kamiokaweb.redo.model.taskitem.TaskItem;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "請求予定明細")
public record InvoiceEstimateItem(
    @Schema(description = "摘要") String description,
    @Schema(description = "数量") Long quantity,
    @Schema(description = "単価") BigDecimal unitPrice,
    @Schema(description = "合計金額") BigDecimal totalPrice
) {
    /**
     * TaskItemとClientから請求予定明細を生成
     * 摘要は「品目名（依頼人名）」形式
     */
    public static InvoiceEstimateItem from(TaskItem taskItem, Client client) {
        String description = formatDescription(taskItem, client);
        return new InvoiceEstimateItem(
            description,
            taskItem.quantity().value().longValue(),
            taskItem.item().unitPrice().value(),
            taskItem.amount().value()
        );
    }

    /**
     * 摘要をフォーマット: 品目名（依頼人名）
     * 依頼人の設定により、依頼人名の表示/非表示を制御
     */
    private static String formatDescription(TaskItem taskItem, Client client) {
        String itemName = taskItem.item().itemName().value();

        // 依頼人名を摘要に表示する設定の場合のみ追加
        if (client.showClientNameInDescription()) {
            String clientName = client.clientName().value();
            return itemName + "（" + clientName + "）";
        }

        return itemName;
    }
}
