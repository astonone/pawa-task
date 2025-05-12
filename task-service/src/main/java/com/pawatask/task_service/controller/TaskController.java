package com.pawatask.task_service.controller;

import com.pawatask.task_service.dto.TaskDto;
import com.pawatask.task_service.mapper.TaskMapper;
import com.pawatask.task_service.model.Task;
import com.pawatask.task_service.repository.TaskRepository;
import com.pawatask.task_service.service.TaskService;
import lombok.RequiredArgsConstructor;
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
        return taskRepository.findAll()
                .stream()
                .map(TaskMapper::toDto)
                .toList();
    }

    @PostMapping
    public TaskDto createTask(
            @RequestBody Task task,
            @RequestHeader("Authorization") String authHeader
    ) {
        Task saved = taskService.createTask(task, authHeader);
        return TaskMapper.toDto(saved);
    }
}
