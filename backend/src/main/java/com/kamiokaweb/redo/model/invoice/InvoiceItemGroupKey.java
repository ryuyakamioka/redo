package com.kamiokaweb.redo.model.invoice;

import java.math.BigDecimal;

import com.kamiokaweb.redo.model.client.Client;
import com.kamiokaweb.redo.model.taskitem.TaskItem;

/**
 * 請求明細を集約するためのキー
 * 同一の依頼人、品目、単価の明細を1つにまとめる際の判定基準
 */
public record InvoiceItemGroupKey(
    Long clientId,
    Long itemId,
    BigDecimal unitPrice
) {
    /**
     * TaskItemとClientから集約キーを生成
     */
    public static InvoiceItemGroupKey from(TaskItem taskItem, Client client) {
        return new InvoiceItemGroupKey(
            client.clientId().value(),
            taskItem.item().itemId().value(),
            taskItem.item().unitPrice().value()
        );
    }

    /**
     * 集約キーの文字列表現
     * （Map のキーとして使用する場合の代替）
     */
    public String toKeyString() {
        return clientId + ":" + itemId + ":" + unitPrice;
    }
}
