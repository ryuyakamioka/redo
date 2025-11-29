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
        TaskDto taskDto;

        // 更新の場合は既存のcreated_atを保持
        if (task.taskId() != null && task.taskId().value() != null && task.taskId().value() != 0) {
            var existingTask = taskAccessor.findById(task.taskId().value());
            if (existingTask.isPresent()) {
                taskDto = new TaskDto(
                    task.taskId().value(),
                    task.taskTitle().value(),
                    task.taskStatus().name(),
                    task.requestDate(),
                    task.client() != null ? task.client().clientId().value() : null,
                    task.note(),
                    task.expectedDeliveryDate(),
                    task.deliveryDate(),
                    existingTask.get().createdAt()  // 既存のcreated_atを保持
                );
            } else {
                taskDto = new TaskDto(task);
            }
        } else {
            taskDto = new TaskDto(task);
        }

        var savedTask = taskAccessor.save(taskDto);

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
        return taskAccessor.findAllByOrderByCreatedAtAsc().stream()
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

    @Override
    @Transactional
    public void revert(TaskId taskId) {
        taskAccessor.findById(taskId.value()).ifPresent(taskDto -> {
            var revertedTask = new TaskDto(
                    taskDto.id(),
                    taskDto.title(),
                    "IN_PROGRESS",
                    taskDto.requestDate(),
                    taskDto.clientId(),
                    taskDto.note(),
                    taskDto.expectedDeliveryDate(),
                    null,
                    taskDto.createdAt()
            );
            taskAccessor.save(revertedTask);
        });
    }
}
