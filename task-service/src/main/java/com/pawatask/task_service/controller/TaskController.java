package com.pawatask.task_service.controller;

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
}
