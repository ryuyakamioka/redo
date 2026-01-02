package com.kamiokaweb.redo.controller.taskstatus;

import com.kamiokaweb.redo.controller.taskstatus.io.TaskStatusListResponse;
import com.kamiokaweb.redo.controller.taskstatus.io.TaskStatusRequest;
import com.kamiokaweb.redo.controller.taskstatus.io.TaskStatusResponse;
import com.kamiokaweb.redo.model.taskstatus.TaskStatusMaster;
import com.kamiokaweb.redo.usecase.TaskStatusUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "依頼ステータス", description = "依頼ステータス関連API")
public class TaskStatusController {
    private final TaskStatusUseCase taskStatusUseCase;

    public TaskStatusController(TaskStatusUseCase taskStatusUseCase) {
        this.taskStatusUseCase = taskStatusUseCase;
    }

    @GetMapping("/task-statuses")
    @Operation(summary = "依頼ステータス一覧取得", description = "全ての依頼ステータスを表示順で取得")
    public TaskStatusListResponse getTaskStatuses() {
        return TaskStatusListResponse.of(taskStatusUseCase.getList());
    }

    @PostMapping("/task-status")
    @Operation(summary = "依頼ステータス登録", description = "新しい依頼ステータスを登録")
    public ResponseEntity<TaskStatusResponse> createTaskStatus(@RequestBody TaskStatusRequest request) {
        TaskStatusMaster created = taskStatusUseCase.create(request.taskStatus());
        return ResponseEntity.ok(TaskStatusResponse.of(created));
    }

    @PutMapping("/task-status/{id}")
    @Operation(summary = "依頼ステータス更新", description = "既存の依頼ステータスを更新")
    public ResponseEntity<TaskStatusResponse> updateTaskStatus(@PathVariable Long id, @RequestBody TaskStatusRequest request) {
        TaskStatusMaster updated = taskStatusUseCase.update(request.taskStatus());
        return ResponseEntity.ok(TaskStatusResponse.of(updated));
    }

    @DeleteMapping("/task-status/{id}")
    @Operation(summary = "依頼ステータス削除", description = "依頼ステータスを削除")
    public ResponseEntity<Void> deleteTaskStatus(@PathVariable Long id) {
        taskStatusUseCase.delete(id);
        return ResponseEntity.ok().build();
    }
}
