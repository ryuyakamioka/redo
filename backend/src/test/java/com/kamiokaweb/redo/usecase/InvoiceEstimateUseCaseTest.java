package com.kamiokaweb.redo.usecase;

import com.kamiokaweb.redo.model.client.Client;
import com.kamiokaweb.redo.model.client.ClientAbbreviation;
import com.kamiokaweb.redo.model.client.ClientId;
import com.kamiokaweb.redo.model.client.ClientName;
import com.kamiokaweb.redo.model.company.Company;
import com.kamiokaweb.redo.model.company.CompanyId;
import com.kamiokaweb.redo.model.company.CompanyName;
import com.kamiokaweb.redo.model.item.Item;
import com.kamiokaweb.redo.model.item.ItemId;
import com.kamiokaweb.redo.model.item.ItemName;
import com.kamiokaweb.redo.model.item.UnitPrice;
import com.kamiokaweb.redo.model.task.Task;
import com.kamiokaweb.redo.model.task.TaskId;
import com.kamiokaweb.redo.model.task.TaskStatus;
import com.kamiokaweb.redo.model.task.TaskTitle;
import com.kamiokaweb.redo.model.taskitem.Amount;
import com.kamiokaweb.redo.model.taskitem.Quantity;
import com.kamiokaweb.redo.model.taskitem.TaskItem;
import com.kamiokaweb.redo.model.taskitem.TaskItemId;
import com.kamiokaweb.redo.repository.task.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class InvoiceEstimateUseCaseTest {

    @Autowired
    private InvoiceEstimateUseCase invoiceEstimateUseCase;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private com.kamiokaweb.redo.infrastructure.task.TaskAccessor taskAccessor;

    private Company company1;
    private Company company2;
    private Client client1;
    private Client client2;
    private Client client3;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        // テスト前にTaskとTaskItemをクリア（マイグレーションデータは保持）
        taskAccessor.deleteAll();

        // テストデータの準備（マイグレーションで挿入済みのデータを参照）
        // V3: 会社ID 1=株式会社サンプル, 2=テスト株式会社, 3=ABC商事
        // V4: 顧客ID 1=顧客A(会社1), 2=顧客B(会社1), 3=顧客C(会社2), 4=顧客D(会社3)
        // V5: 品目ID 1=データ入力(500円), 2=書類作成(1000円), 3=校正作業(800円), 4=翻訳作業(1500円)
        company1 = new Company(
                new CompanyId(1L),
                new CompanyName("株式会社サンプル"),
                true,
                null
        );
        company2 = new Company(
                new CompanyId(2L),
                new CompanyName("テスト株式会社"),
                false,
                null
        );
        client1 = new Client(
                new ClientId(1L),
                new ClientName("顧客A"),
                new ClientAbbreviation("A"),
                company1,
                true
        );
        client2 = new Client(
                new ClientId(2L),
                new ClientName("顧客B"),
                new ClientAbbreviation("B"),
                company1,
                true
        );
        client3 = new Client(
                new ClientId(3L),
                new ClientName("顧客C"),
                new ClientAbbreviation("C"),
                company2,
                true
        );
        item1 = new Item(
                new ItemId(1L),
                new ItemName("データ入力"),
                new UnitPrice(new BigDecimal("500"))
        );
        item2 = new Item(
                new ItemId(2L),
                new ItemName("書類作成"),
                new UnitPrice(new BigDecimal("1000"))
        );
    }

    @Test
    void getEstimates_完了済み依頼から請求予定を取得できる() {
        // Arrange
        LocalDate deliveryDate = LocalDate.of(2025, 12, 15);

        // 完了済み依頼を作成
        var task1 = createTask(client1, deliveryDate, TaskStatus.DONE, List.of(
                createTaskItem(item1, 5)
        ));
        taskRepository.register(task1);

        // Act
        var estimates = invoiceEstimateUseCase.getEstimates("202512", null);

        // Assert
        assertThat(estimates).isNotEmpty();
        var estimate = estimates.stream()
                .filter(e -> e.companyId().value().equals(1L))
                .findFirst();
        assertThat(estimate).isPresent();
    }

    @Test
    void getEstimates_同一品目と依頼人の明細をまとめる() {
        // Arrange
        LocalDate deliveryDate = LocalDate.of(2026, 1, 15);

        // 同じ依頼人、同じ品目の依頼を複数作成
        var task1 = createTask(client1, deliveryDate, TaskStatus.DONE, List.of(
                createTaskItem(item1, 5)
        ));
        var task2 = createTask(client1, deliveryDate, TaskStatus.DONE, List.of(
                createTaskItem(item1, 3)
        ));
        taskRepository.register(task1);
        taskRepository.register(task2);

        // Act
        var estimates = invoiceEstimateUseCase.getEstimates("202601", null);

        // Assert
        var estimate = estimates.stream()
                .filter(e -> e.companyId().value().equals(1L))
                .findFirst()
                .orElseThrow();

        // 明細が1つにまとめられている
        assertThat(estimate.items()).hasSize(1);
        var item = estimate.items().get(0);
        assertThat(item.quantity()).isEqualTo(8L); // 5 + 3
        assertThat(item.totalPrice()).isEqualByComparingTo(new BigDecimal("4000")); // 500 * 8
    }

    @Test
    void getEstimates_依頼人IDと品目IDでソートされる() {
        // Arrange
        LocalDate deliveryDate = LocalDate.of(2025, 12, 15);

        // 複数の依頼人と品目の組み合わせで依頼を作成
        var task = createTask(client1, deliveryDate, TaskStatus.DONE, List.of(
                createTaskItem(item2, 1), // 品目ID: 2
                createTaskItem(item1, 5)  // 品目ID: 1
        ));
        var task2 = createTask(client2, deliveryDate, TaskStatus.DONE, List.of(
                createTaskItem(item1, 2)
        ));
        taskRepository.register(task);
        taskRepository.register(task2);

        // Act
        var estimates = invoiceEstimateUseCase.getEstimates("202512", null);

        // Assert
        var estimate = estimates.stream()
                .filter(e -> e.companyId().value().equals(1L))
                .findFirst()
                .orElseThrow();

        var items = estimate.items();
        assertThat(items).hasSize(3);

        // 依頼人ID順、品目ID順にソートされているか確認
        // 1番目: 顧客A(clientId=1) + データ入力(itemId=1)
        assertThat(items.get(0).description()).contains("顧客A").contains("データ入力");
        // 2番目: 顧客A(clientId=1) + 書類作成(itemId=2)
        assertThat(items.get(1).description()).contains("顧客A").contains("書類作成");
        // 3番目: 顧客B(clientId=2) + データ入力(itemId=1)
        assertThat(items.get(2).description()).contains("顧客B").contains("データ入力");
    }

    @Test
    void getEstimates_締日以降の依頼は含まれない() {
        // Arrange
        LocalDate deliveryDateBefore = LocalDate.of(2026, 2, 20); // 締日
        LocalDate deliveryDateAfter = LocalDate.of(2026, 2, 21);  // 締日の翌日

        var task1 = createTask(client1, deliveryDateBefore, TaskStatus.DONE, List.of(
                createTaskItem(item1, 5)
        ));
        var task2 = createTask(client1, deliveryDateAfter, TaskStatus.DONE, List.of(
                createTaskItem(item2, 1)
        ));
        taskRepository.register(task1);
        taskRepository.register(task2);

        // Act
        var estimates = invoiceEstimateUseCase.getEstimates("202602", null);

        // Assert
        var estimate = estimates.stream()
                .filter(e -> e.companyId().value().equals(1L))
                .findFirst()
                .orElseThrow();

        // 締日以前の依頼のみが含まれる
        assertThat(estimate.items()).hasSize(1);
        assertThat(estimate.items().get(0).description()).contains("データ入力");
    }

    @Test
    void getEstimates_未完了の依頼は含まれない() {
        // Arrange
        LocalDate deliveryDate = LocalDate.of(2026, 3, 15);

        var task1 = createTask(client1, deliveryDate, TaskStatus.DONE, List.of(
                createTaskItem(item1, 5)
        ));
        var task2 = createTask(client1, deliveryDate, TaskStatus.IN_PROGRESS, List.of(
                createTaskItem(item2, 1)
        ));
        taskRepository.register(task1);
        taskRepository.register(task2);

        // Act
        var estimates = invoiceEstimateUseCase.getEstimates("202603", null);

        // Assert
        var estimate = estimates.stream()
                .filter(e -> e.companyId().value().equals(1L))
                .findFirst()
                .orElseThrow();

        // 完了済みの依頼のみが含まれる
        assertThat(estimate.items()).hasSize(1);
        assertThat(estimate.items().get(0).description()).contains("データ入力");
    }

    @Test
    void getEstimates_会社IDで絞り込みできる() {
        // Arrange
        LocalDate deliveryDate = LocalDate.of(2025, 12, 15);

        var task1 = createTask(client1, deliveryDate, TaskStatus.DONE, List.of(
                createTaskItem(item1, 5)
        ));
        var task2 = createTask(client3, deliveryDate, TaskStatus.DONE, List.of(
                createTaskItem(item2, 1)
        ));
        taskRepository.register(task1);
        taskRepository.register(task2);

        // Act
        var estimates = invoiceEstimateUseCase.getEstimates("202512", 1L);

        // Assert
        assertThat(estimates).hasSize(1);
        assertThat(estimates.get(0).companyId().value()).isEqualTo(1L);
    }

    @Test
    void getEstimates_小計が正しく計算される() {
        // Arrange
        LocalDate deliveryDate = LocalDate.of(2026, 4, 15);

        var task = createTask(client1, deliveryDate, TaskStatus.DONE, List.of(
                createTaskItem(item1, 5),  // 500 * 5 = 2500
                createTaskItem(item2, 2)   // 1000 * 2 = 2000
        ));
        taskRepository.register(task);

        // Act
        var estimates = invoiceEstimateUseCase.getEstimates("202604", null);

        // Assert
        var estimate = estimates.stream()
                .filter(e -> e.companyId().value().equals(1L))
                .findFirst()
                .orElseThrow();

        assertThat(estimate.subtotal()).isEqualByComparingTo(new BigDecimal("4500")); // 2500 + 2000
    }

    private Task createTask(Client client, LocalDate deliveryDate, TaskStatus status, List<TaskItem> taskItems) {
        return new Task(
                new TaskId(0L),
                new TaskTitle("テスト依頼"),
                status,
                LocalDate.now(),
                client,
                null,
                taskItems,
                deliveryDate,
                deliveryDate
        );
    }

    private TaskItem createTaskItem(Item item, Integer quantity) {
        BigDecimal quantityBigDecimal = new BigDecimal(quantity);
        return new TaskItem(
                new TaskItemId(null),
                item,
                new Quantity(quantity),
                new Amount(item.unitPrice().value().multiply(quantityBigDecimal))
        );
    }
}
