package com.pawatask.task_service.service;

import com.pawatask.task_service.client.UserClient;
import com.pawatask.task_service.dto.UserInfo;
import com.pawatask.task_service.model.Task;
import com.pawatask.task_service.repository.TaskRepository;
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
}
