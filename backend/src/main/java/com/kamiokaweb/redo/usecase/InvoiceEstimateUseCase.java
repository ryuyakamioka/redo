package com.kamiokaweb.redo.usecase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kamiokaweb.redo.model.company.CompanyId;
import com.kamiokaweb.redo.model.invoice.AccountingPeriod;
import com.kamiokaweb.redo.model.invoice.InvoiceEstimate;
import com.kamiokaweb.redo.model.invoice.InvoiceEstimateItem;
import com.kamiokaweb.redo.model.invoice.InvoiceEstimateItemAggregator;
import com.kamiokaweb.redo.model.task.Task;
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
     * 指定された請求月・会社IDに対応する依頼IDリストを取得する
     *
     * @param billingMonth 請求予定年月（形式: "202512"）
     * @param companyId 会社ID
     * @return 依頼IDリスト
     */
    public List<Long> getTaskIdsForInvoice(String billingMonth, Long companyId) {
        // 請求予定年月をパース
        Year year = Year.parse(billingMonth.substring(0, 4));
        Month month = Month.of(Integer.parseInt(billingMonth.substring(4, 6)));

        // 締日を計算（当月20日）
        LocalDate closingDate = LocalDate.of(year.getValue(), month.getValue(), CLOSING_DAY);

        // 開始日を計算（前月21日）
        LocalDate startDate = closingDate.minusMonths(1).withDayOfMonth(CLOSING_DAY + 1);

        // 全ての完了済み依頼を取得
        var allTasks = taskRepository.getList();

        // フィルタリング: 完了済み、納品日が期間内（前月21日〜当月20日）、会社IDが一致、請求日がnull
        return allTasks.stream()
            .filter(task -> "DONE".equals(task.taskStatus().name()))
            .filter(task -> task.deliveryDate() != null &&
                !task.deliveryDate().isBefore(startDate) &&
                !task.deliveryDate().isAfter(closingDate))
            .filter(task -> task.client() != null && task.client().company().companyId().value().equals(companyId))
            .filter(task -> task.billingDate() == null) // 未請求のもののみ
            .map(task -> task.taskId().value())
            .toList();
    }

    /**
     * 請求予定を取得する
     *
     * @param billingMonth 請求予定年月（形式: "202512"）
     * @param companyId 会社ID（任意）
     * @param onlyUnbilled 未請求のみ取得（任意、デフォルトfalse）
     * @return 請求予定リスト
     */
    public List<InvoiceEstimate> getEstimates(String billingMonth, Long companyId, Boolean onlyUnbilled) {
        // 請求予定年月をパース
        Year year = Year.parse(billingMonth.substring(0, 4));
        Month month = Month.of(Integer.parseInt(billingMonth.substring(4, 6)));
        AccountingPeriod accountingPeriod = new AccountingPeriod(year, month);

        // 締日を計算（当月20日）
        LocalDate closingDate = LocalDate.of(year.getValue(), month.getValue(), CLOSING_DAY);

        // 開始日を計算（前月21日）
        LocalDate startDate = closingDate.minusMonths(1).withDayOfMonth(CLOSING_DAY + 1);

        // 全ての完了済み依頼を取得
        var allTasks = taskRepository.getList();

        // フィルタリング: 完了済み、納品日が期間内（前月21日〜当月20日）、会社IDが一致（指定されている場合）、未請求のみ（指定されている場合）
        var filteredTasks = allTasks.stream()
            .filter(task -> "DONE".equals(task.taskStatus().name()))
            .filter(task -> task.deliveryDate() != null &&
                !task.deliveryDate().isBefore(startDate) &&
                !task.deliveryDate().isAfter(closingDate))
            .filter(task -> companyId == null ||
                (task.client() != null && task.client().company().companyId().value().equals(companyId)))
            .filter(task -> onlyUnbilled == null || !onlyUnbilled || task.billingDate() == null)
            .toList();

        // 会社ごとにグループ化
        Map<Long, List<Task>> tasksByCompany = filteredTasks.stream()
            .filter(task -> task.client() != null && task.client().company() != null)
            .collect(Collectors.groupingBy(task -> task.client().company().companyId().value()));

        // 各会社ごとに請求予定を作成
        return tasksByCompany.entrySet().stream()
            .map(entry -> {
                Long targetCompanyId = entry.getKey();
                var tasks = entry.getValue();

                // ドメインサービスで明細を集約・ソート
                var items = InvoiceEstimateItemAggregator.aggregateAndSort(tasks);

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
