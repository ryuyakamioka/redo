package com.kamiokaweb.redo.model.invoice;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.kamiokaweb.redo.model.task.Task;

/**
 * 請求予定明細の集約とソートを行うドメインサービス
 */
public class InvoiceEstimateItemAggregator {

    /**
     * 中間データ構造（集約とソートに必要な情報を保持）
     */
    private record ItemWithIds(
        Long clientId,
        Long itemId,
        String description,
        Long quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
    ) {}

    /**
     * Taskリストから請求予定明細を生成し、集約・ソートして返す
     *
     * ビジネスルール:
     * - 同一の依頼人、品目、単価の明細は1つにまとめる
     * - 依頼人IDの昇順、品目IDの昇順でソート
     */
    public static List<InvoiceEstimateItem> aggregateAndSort(List<Task> tasks) {

        // 全てのTaskItemから中間データを作成
        var allItems = tasks.stream()
            .flatMap(task -> task.taskItems().stream()
                .map(taskItem -> {
                    Long clientId = task.client() != null ? task.client().clientId().value() : 0L;
                    InvoiceEstimateItem item = InvoiceEstimateItem.from(taskItem, task.client());

                    return new ItemWithIds(
                        clientId,
                        taskItem.item().itemId().value(),
                        item.description(),
                        item.quantity(),
                        item.unitPrice(),
                        item.totalPrice()
                    );
                })
            )
            .toList();

        // 依頼人ID、品目ID、単価が同じ明細をまとめる
        var groupedItems = allItems.stream()
            .collect(Collectors.groupingBy(
                item -> new InvoiceItemGroupKey(item.clientId(), item.itemId(), item.unitPrice()).toKeyString(),
                Collectors.reducing((item1, item2) -> {
                    long totalQuantity = item1.quantity() + item2.quantity();
                    BigDecimal totalPrice = item1.unitPrice().multiply(BigDecimal.valueOf(totalQuantity));
                    return new ItemWithIds(
                        item1.clientId(),
                        item1.itemId(),
                        item1.description(),
                        totalQuantity,
                        item1.unitPrice(),
                        totalPrice
                    );
                })
            ))
            .values().stream()
            .filter(optional -> optional.isPresent())
            .map(optional -> optional.get())
            .toList();

        // 依頼人IDの昇順、品目IDの昇順でソート
        return groupedItems.stream()
            .sorted(itemComparator())
            .map(item -> new InvoiceEstimateItem(
                item.description(),
                item.quantity(),
                item.unitPrice(),
                item.totalPrice()
            ))
            .toList();
    }

    /**
     * 請求予定明細のソート順を定義
     * 1. 依頼人ID昇順
     * 2. 品目ID昇順
     */
    private static Comparator<ItemWithIds> itemComparator() {
        return Comparator.comparing(ItemWithIds::clientId)
                         .thenComparing(ItemWithIds::itemId);
    }
}
