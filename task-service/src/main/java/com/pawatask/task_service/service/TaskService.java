package com.pawatask.task_service.service;

import com.pawatask.task_service.client.UserClient;
import com.pawatask.task_service.dto.CommentDto;
import com.pawatask.task_service.dto.TaskDto;
import com.pawatask.task_service.dto.UserInfo;
import com.pawatask.task_service.mapper.TaskMapper;
import com.pawatask.task_service.model.Comment;
import com.pawatask.task_service.model.Task;
import com.pawatask.task_service.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserClient userClient;

    public Task createTask(TaskDto dto, String token) {
        UserInfo user = userClient.getCurrentUser(token);

        if (dto.getTodoDate().toLocalDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Task date cannot be in the past");
        }

        Task task = TaskMapper.fromDto(dto);
        task.setCreatedBy(user.getUsername());
        task.setLastEditedBy(user.getUsername());
        task.setDone(false);

        if (task.getComments() != null) {
            task.getComments().forEach(c -> c.setTask(task));
        }

        return taskRepository.save(task);
    }

    public void updateTask(Long id, TaskDto dto, String token) {
        UserInfo user = userClient.getCurrentUser(token);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (dto.getTodoDate().toLocalDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Task date cannot be in the past");
        }

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());
        task.setTodoDate(dto.getTodoDate());
        task.setLastEditedBy(user.getUsername());

        taskRepository.save(task);
    }

    public void toggleTaskDone(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        task.setDone(!task.isDone());
        taskRepository.save(task);
    }

    public Task addComment(Long taskId, CommentDto dto, String token) {
        UserInfo user = userClient.getCurrentUser(token);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        Comment comment = Comment.builder()
                .text(dto.getText())
                .author(user.getUsername())
                .createdAt(LocalDateTime.now())
                .task(task)
                .build();

        task.getComments().add(comment);
        return taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
    }
}
