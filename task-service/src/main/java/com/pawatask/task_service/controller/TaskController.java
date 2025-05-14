package com.pawatask.task_service.controller;

import com.pawatask.task_service.dto.CommentDto;
import com.pawatask.task_service.dto.TaskDto;
import com.pawatask.task_service.mapper.TaskMapper;
import com.pawatask.task_service.model.Task;
import com.pawatask.task_service.repository.TaskRepository;
import com.pawatask.task_service.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;
    private final TaskService taskService;

    @GetMapping
    public List<TaskDto> getAllTasks() {
        return taskRepository.findAllByOrderByIsDoneAscTodoDateAsc()
                .stream()
                .map(TaskMapper::toDto)
                .toList();
    }

    @PostMapping
    public TaskDto createTask(
            @Valid @RequestBody TaskDto dto,
            @RequestHeader("Authorization") String token
    ) {
        Task saved = taskService.createTask(dto, token);
        return TaskMapper.toDto(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody @Valid TaskDto dto, @RequestHeader("Authorization") String token) {
        taskService.updateTask(id, dto, token);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggleTaskDone(@PathVariable Long id) {
        taskService.toggleTaskDone(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long id,
                                        @Valid @RequestBody CommentDto dto,
                                        @RequestHeader("Authorization") String token) {
        Task task = taskService.addComment(id, dto, token);
        return ResponseEntity.ok(TaskMapper.toDto(task));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(TaskMapper.toDto(task));
    }
}
