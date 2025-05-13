package com.pawatask.task_service.service;

import com.pawatask.task_service.client.UserClient;
import com.pawatask.task_service.dto.TaskDto;
import com.pawatask.task_service.dto.UserInfo;
import com.pawatask.task_service.model.Task;
import com.pawatask.task_service.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserClient userClient;

    public Task createTask(Task task, String token) {
        UserInfo user = userClient.getCurrentUser(token);

        task.setCreatedBy(user.getUsername());
        task.setLastEditedBy(user.getUsername());

        if (task.getComments() != null) {
            task.getComments().forEach(comment -> comment.setTask(task));
        }

        return taskRepository.save(task);
    }

    public void updateTask(Long id, TaskDto dto, String token) {
        UserInfo user = userClient.getCurrentUser(token);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

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
}
