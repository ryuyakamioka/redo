package com.kamiokaweb.redo.repository.task;

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
class TaskRepositoryImplTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void register_正常に依頼を登録できる() {
        // Arrange
        var company = new Company(
                new CompanyId(1L),
                new CompanyName("株式会社サンプル"),
                true,
                null
        );
        var client = new Client(
                new ClientId(1L),
                new ClientName("顧客A"),
                new ClientAbbreviation("A"),
                company,
                true
        );
        var item = new Item(
                new ItemId(1L),
                new ItemName("データ入力"),
                new UnitPrice(new BigDecimal("500.00"))
        );
        var taskItem = new TaskItem(
                null,
                item,
                new Quantity(10),
                new Amount(new BigDecimal("5000.00"))
        );
        var task = new Task(
                new TaskId(0L),
                new TaskTitle("テスト依頼"),
                TaskStatus.TODO,
                LocalDate.now(),
                client,
                "テストメモ",
                List.of(taskItem),
                LocalDate.now().plusDays(3),
                null,
                null
        );

        // Act
        taskRepository.register(task);

        // Assert
        var tasks = taskRepository.getList();
        assertThat(tasks).isNotEmpty();
        var savedTask = tasks.stream()
                .filter(t -> t.taskTitle().value().equals("テスト依頼"))
                .findFirst();
        assertThat(savedTask).isPresent();
        assertThat(savedTask.get().client()).isNotNull();
        assertThat(savedTask.get().client().clientName().value()).isEqualTo("顧客A");
        assertThat(savedTask.get().taskItems()).hasSize(1);
        assertThat(savedTask.get().taskItems().get(0).item().itemName().value()).isEqualTo("データ入力");
    }

    @Test
    void getList_依頼一覧を取得できる() {
        // Act
        var tasks = taskRepository.getList();

        // Assert
        assertThat(tasks).isNotNull();
    }

    @Test
    void get_IDで依頼を取得できる() {
        // Arrange
        var company = new Company(
                new CompanyId(1L),
                new CompanyName("株式会社サンプル"),
                true,
                null
        );
        var client = new Client(
                new ClientId(1L),
                new ClientName("顧客A"),
                new ClientAbbreviation("A"),
                company,
                true
        );
        var task = new Task(
                new TaskId(0L),
                new TaskTitle("取得テスト依頼"),
                TaskStatus.TODO,
                LocalDate.now(),
                client,
                null,
                List.of(),
                LocalDate.now().plusDays(3),
                null,
                null
        );
        taskRepository.register(task);

        // Act
        var tasks = taskRepository.getList();
        var registeredTask = tasks.stream()
                .filter(t -> t.taskTitle().value().equals("取得テスト依頼"))
                .findFirst()
                .orElseThrow();
        var result = taskRepository.get(registeredTask.taskId());

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().taskTitle().value()).isEqualTo("取得テスト依頼");
    }

    @Test
    void delete_依頼を削除できる() {
        // Arrange
        var company = new Company(
                new CompanyId(1L),
                new CompanyName("株式会社サンプル"),
                true,
                null
        );
        var client = new Client(
                new ClientId(1L),
                new ClientName("顧客A"),
                new ClientAbbreviation("A"),
                company,
                true
        );
        var task = new Task(
                new TaskId(0L),
                new TaskTitle("削除テスト依頼"),
                TaskStatus.TODO,
                LocalDate.now(),
                client,
                null,
                List.of(),
                LocalDate.now().plusDays(3),
                null,
                null
        );
        taskRepository.register(task);
        var tasks = taskRepository.getList();
        var registeredTask = tasks.stream()
                .filter(t -> t.taskTitle().value().equals("削除テスト依頼"))
                .findFirst()
                .orElseThrow();

        // Act
        taskRepository.delete(registeredTask.taskId());

        // Assert
        var result = taskRepository.get(registeredTask.taskId());
        assertThat(result).isEmpty();
    }
}
