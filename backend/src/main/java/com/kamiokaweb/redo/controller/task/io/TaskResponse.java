package com.kamiokaweb.redo.controller.task.io;

import com.kamiokaweb.redo.model.task.Task;

public record TaskResponse(
        Task task
) {
    public static TaskResponse of(Task task) {
        return new TaskResponse(task);
    }
}
