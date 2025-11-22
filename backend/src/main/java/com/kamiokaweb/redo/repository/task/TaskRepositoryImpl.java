package com.kamiokaweb.redo.repository.task;

import com.kamiokaweb.redo.infrastructure.client.ClientAccessor;
import com.kamiokaweb.redo.infrastructure.company.CompanyAccessor;
import com.kamiokaweb.redo.infrastructure.item.ItemAccessor;
import com.kamiokaweb.redo.infrastructure.task.TaskAccessor;
import com.kamiokaweb.redo.infrastructure.task.TaskDto;
import com.kamiokaweb.redo.infrastructure.taskitem.TaskItemAccessor;
import com.kamiokaweb.redo.infrastructure.taskitem.TaskItemDto;
import com.kamiokaweb.redo.model.task.Task;
import com.kamiokaweb.redo.model.task.TaskId;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Repository
public class TaskRepositoryImpl implements TaskRepository {
    private final TaskAccessor taskAccessor;
    private final ClientAccessor clientAccessor;
    private final CompanyAccessor companyAccessor;
    private final ItemAccessor itemAccessor;
    private final TaskItemAccessor taskItemAccessor;

    public TaskRepositoryImpl(
            TaskAccessor taskAccessor,
            ClientAccessor clientAccessor,
            CompanyAccessor companyAccessor,
            ItemAccessor itemAccessor,
            TaskItemAccessor taskItemAccessor
    ) {
        this.taskAccessor = taskAccessor;
        this.clientAccessor = clientAccessor;
        this.companyAccessor = companyAccessor;
        this.itemAccessor = itemAccessor;
        this.taskItemAccessor = taskItemAccessor;
    }

    @Override
    @Transactional
    public void register(Task task) {
        var savedTask = taskAccessor.save(new TaskDto(task));

        // 既存の依頼明細を削除（更新の場合）
        if (task.taskId() != null && task.taskId().value() != null) {
            taskItemAccessor.deleteByTaskId(task.taskId().value());
        }

        // 依頼明細を保存
        if (task.taskItems() != null && !task.taskItems().isEmpty()) {
            task.taskItems().forEach(taskItem -> {
                taskItemAccessor.save(new TaskItemDto(savedTask.id(), taskItem));
            });
        }
    }

    @Override
    public Optional<Task> get(TaskId taskId) {
        return taskAccessor.findById(taskId.value())
                .map(taskDto -> {
                    var client = taskDto.clientId() != null
                            ? clientAccessor.findById(taskDto.clientId())
                                    .flatMap(clientDto -> companyAccessor.findById(clientDto.companyId())
                                            .map(companyDto -> clientDto.from(companyDto.from())))
                                    .orElse(null)
                            : null;

                    var taskItems = taskItemAccessor.findByTaskId(taskDto.id()).stream()
                            .map(taskItemDto -> itemAccessor.findById(taskItemDto.itemId())
                                    .map(itemDto -> taskItemDto.from(itemDto.from()))
                                    .orElseThrow())
                            .toList();

                    return taskDto.from(client, taskItems);
                });
    }

    @Override
    public List<Task> getList() {
        return StreamSupport.stream(taskAccessor.findAll().spliterator(), false)
                .map(taskDto -> {
                    var client = taskDto.clientId() != null
                            ? clientAccessor.findById(taskDto.clientId())
                                    .flatMap(clientDto -> companyAccessor.findById(clientDto.companyId())
                                            .map(companyDto -> clientDto.from(companyDto.from())))
                                    .orElse(null)
                            : null;

                    var taskItems = taskItemAccessor.findByTaskId(taskDto.id()).stream()
                            .map(taskItemDto -> itemAccessor.findById(taskItemDto.itemId())
                                    .map(itemDto -> taskItemDto.from(itemDto.from()))
                                    .orElseThrow())
                            .toList();

                    return taskDto.from(client, taskItems);
                })
                .toList();
    }

    @Override
    @Transactional
    public void delete(TaskId taskId) {
        taskItemAccessor.deleteByTaskId(taskId.value());
        taskAccessor.findById(taskId.value()).ifPresent(taskAccessor::delete);
    }

    @Override
    @Transactional
    public void complete(TaskId taskId) {
        taskAccessor.findById(taskId.value()).ifPresent(taskDto -> {
            var completedTask = new TaskDto(
                    taskDto.id(),
                    taskDto.title(),
                    "DONE",
                    taskDto.requestDate(),
                    taskDto.clientId(),
                    taskDto.note(),
                    taskDto.expectedDeliveryDate(),
                    java.time.LocalDate.now(),
                    taskDto.createdAt()
            );
            taskAccessor.save(completedTask);
        });
    }
}
