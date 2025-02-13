package com.kamiokaweb.redo.controller.task;

import com.kamiokaweb.redo.controller.task.io.TaskListResponse;
import com.kamiokaweb.redo.controller.task.io.TaskRequest;
import com.kamiokaweb.redo.controller.task.io.TaskResponse;
import com.kamiokaweb.redo.model.task.TaskId;
import com.kamiokaweb.redo.usecase.TaskUseCase;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {

    private final TaskUseCase taskUseCase;

    public TaskController(TaskUseCase taskUseCase) {
        this.taskUseCase = taskUseCase;
    }

    @GetMapping("/task/{taskId}")
    public TaskResponse getTask(@PathVariable Long taskId) {
        return TaskResponse.of(taskUseCase.get(new TaskId(taskId)));
    }

    @GetMapping("/tasks")
    public TaskListResponse getTasks() {
        return TaskListResponse.of(taskUseCase.getList());
    }

    @PostMapping("/task")
    public void registerTask(@RequestBody TaskRequest taskRequest) {
        taskUseCase.register(taskRequest.task());
    }
}
