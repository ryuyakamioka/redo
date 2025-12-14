package com.kamiokaweb.redo.usecase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kamiokaweb.redo.model.company.CompanyId;
import com.kamiokaweb.redo.model.invoice.AccountingPeriod;
import com.kamiokaweb.redo.model.invoice.InvoiceEstimate;
import com.kamiokaweb.redo.model.invoice.InvoiceEstimateItem;
import com.kamiokaweb.redo.repository.task.TaskRepository;

@Service
public class InvoiceEstimateUseCase {
    private final TaskRepository taskRepository;

    // 締日（固定値）
    private static final int CLOSING_DAY = 20;

    public InvoiceEstimateUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * 請求予定を取得する
     *
     * @param billingMonth 請求予定年月（形式: "202512"）
     * @param companyId 会社ID（任意）
     * @return 請求予定リスト
     */
    public List<InvoiceEstimate> getEstimates(String billingMonth, Long companyId) {
        // 請求予定年月をパース
        Year year = Year.parse(billingMonth.substring(0, 4));
        Month month = Month.of(Integer.parseInt(billingMonth.substring(4, 6)));
        AccountingPeriod accountingPeriod = new AccountingPeriod(year, month);

        // 締日を計算
        LocalDate closingDate = LocalDate.of(year.getValue(), month.getValue(), CLOSING_DAY);

        // 全ての完了済み依頼を取得
        var allTasks = taskRepository.getList();

        // フィルタリング: 完了済み、納品日が締日以前、会社IDが一致（指定されている場合）
        var filteredTasks = allTasks.stream()
            .filter(task -> "DONE".equals(task.taskStatus().name()))
            .filter(task -> task.deliveryDate() != null && !task.deliveryDate().isAfter(closingDate))
            .filter(task -> companyId == null ||
                (task.client() != null && task.client().company().companyId().value().equals(companyId)))
            .toList();

        // 会社ごとにグループ化
        Map<Long, List<com.kamiokaweb.redo.model.task.Task>> tasksByCompany = filteredTasks.stream()
            .filter(task -> task.client() != null && task.client().company() != null)
            .collect(Collectors.groupingBy(task -> task.client().company().companyId().value()));

        // 各会社ごとに請求予定を作成
        return tasksByCompany.entrySet().stream()
            .map(entry -> {
                Long targetCompanyId = entry.getKey();
                var tasks = entry.getValue();

                // 明細を作成（IDを保持した中間データ構造を使用）
                record ItemWithIds(Long clientId, Long itemId, String description, Long quantity, BigDecimal unitPrice, BigDecimal totalPrice) {}

                var allItems = tasks.stream()
                    .flatMap(task -> task.taskItems().stream()
                        .map(taskItem -> {
                            // 摘要: 品目名(依頼人名)
                            Long clientId = task.client() != null ? task.client().clientId().value() : 0L;
                            String clientName = task.client() != null ? task.client().clientName().value() : "";
                            String description = taskItem.item().itemName().value() + "（" + clientName + "）";
                            Long itemId = taskItem.item().itemId().value();

                            return new ItemWithIds(
                                clientId,
                                itemId,
                                description,
                                taskItem.quantity().value().longValue(),
                                taskItem.item().unitPrice().value(),
                                taskItem.amount().value()
                            );
                        })
                    )
                    .toList();

                // 依頼人ID、品目ID、単価が同じ明細をまとめる
                var groupedItems = allItems.stream()
                    .collect(Collectors.groupingBy(
                        item -> item.clientId() + ":" + item.itemId() + ":" + item.unitPrice(),
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
                var items = groupedItems.stream()
                    .sorted(Comparator.comparing(ItemWithIds::clientId)
                        .thenComparing(ItemWithIds::itemId))
                    .map(item -> new InvoiceEstimateItem(
                        item.description(),
                        item.quantity(),
                        item.unitPrice(),
                        item.totalPrice()
                    ))
                    .toList();

                // 小計を計算
                BigDecimal subtotal = items.stream()
                    .map(InvoiceEstimateItem::totalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                return new InvoiceEstimate(
                    new CompanyId(targetCompanyId),
                    accountingPeriod,
                    subtotal,
                    items
                );
            })
            .toList();
    }
}
