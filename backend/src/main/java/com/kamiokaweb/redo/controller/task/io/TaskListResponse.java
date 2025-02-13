package com.kamiokaweb.redo.controller.task.io;

import com.kamiokaweb.redo.model.task.Task;

import java.util.List;

public record TaskListResponse(
        List<Task> taskList
) {
    public static TaskListResponse of(List<Task> taskList) {
        return new TaskListResponse(taskList);
    }
}
