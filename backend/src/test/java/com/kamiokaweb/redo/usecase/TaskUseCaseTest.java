package com.kamiokaweb.redo.usecase;

import com.kamiokaweb.redo.model.client.Client;
import com.kamiokaweb.redo.model.client.ClientAbbreviation;
import com.kamiokaweb.redo.model.client.ClientId;
import com.kamiokaweb.redo.model.client.ClientName;
import com.kamiokaweb.redo.model.company.Company;
import com.kamiokaweb.redo.model.company.CompanyId;
import com.kamiokaweb.redo.model.company.CompanyName;
import com.kamiokaweb.redo.model.task.Task;
import com.kamiokaweb.redo.model.task.TaskId;
import com.kamiokaweb.redo.model.task.TaskStatus;
import com.kamiokaweb.redo.model.task.TaskTitle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TaskUseCaseTest {

    @Autowired
    private TaskUseCase taskUseCase;

    @Test
    void register_正常に依頼を登録できる() {
        // Arrange
        var company = new Company(
                new CompanyId(1L),
                new CompanyName("株式会社サンプル"),
                true
        );
        var client = new Client(
                new ClientId(1L),
                new ClientName("顧客A"),
                new ClientAbbreviation("A"),
                company
        );
        var task = new Task(
                new TaskId(0L),
                new TaskTitle("UseCaseテスト依頼"),
                TaskStatus.TODO,
                LocalDate.now(),
                client,
                "UseCaseテストメモ",
                List.of(),
                LocalDate.now().plusDays(3),
                null
        );

        // Act
        taskUseCase.register(task);

        // Assert
        var tasks = taskUseCase.getList();
        assertThat(tasks).isNotEmpty();
        var savedTask = tasks.stream()
                .filter(t -> t.taskTitle().value().equals("UseCaseテスト依頼"))
                .findFirst();
        assertThat(savedTask).isPresent();
    }

    @Test
    void getList_依頼一覧を取得できる() {
        // Act
        var tasks = taskUseCase.getList();

        // Assert
        assertThat(tasks).isNotNull();
    }

    @Test
    void get_IDで依頼を取得できる() {
        // Arrange: マイグレーションで挿入されたデータがあれば、それを利用
        var tasks = taskUseCase.getList();
        if (!tasks.isEmpty()) {
            var firstTask = tasks.get(0);

            // Act
            var result = taskUseCase.get(firstTask.taskId());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.taskId()).isEqualTo(firstTask.taskId());
        }
    }

    @Test
    void delete_依頼を削除できる() {
        // Arrange
        var company = new Company(
                new CompanyId(1L),
                new CompanyName("株式会社サンプル"),
                true
        );
        var client = new Client(
                new ClientId(1L),
                new ClientName("顧客A"),
                new ClientAbbreviation("A"),
                company
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
                null
        );
        taskUseCase.register(task);
        var tasks = taskUseCase.getList();
        var registeredTask = tasks.stream()
                .filter(t -> t.taskTitle().value().equals("削除テスト依頼"))
                .findFirst()
                .orElseThrow();

        // Act
        taskUseCase.delete(registeredTask.taskId());

        // Assert
        var afterDelete = taskUseCase.getList();
        var deleted = afterDelete.stream()
                .filter(t -> t.taskId().equals(registeredTask.taskId()))
                .findFirst();
        assertThat(deleted).isEmpty();
    }
}
