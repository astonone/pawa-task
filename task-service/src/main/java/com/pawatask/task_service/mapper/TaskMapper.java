package com.pawatask.task_service.mapper;

import com.pawatask.task_service.dto.CommentDto;
import com.pawatask.task_service.dto.TaskDto;
import com.pawatask.task_service.model.Comment;
import com.pawatask.task_service.model.Task;

import java.util.stream.Collectors;

public class TaskMapper {

    public static TaskDto toDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setCreatedBy(task.getCreatedBy());
        dto.setLastEditedBy(task.getLastEditedBy());
        dto.setTodoDate(task.getTodoDate());
        dto.setPriority(task.getPriority());
        dto.setDone(task.isDone());

        if (task.getComments() != null) {
            dto.setComments(task.getComments().stream().map(TaskMapper::commentToDto).collect(Collectors.toList()));
        }

        return dto;
    }

    private static CommentDto commentToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthor(comment.getAuthor());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }

    public static Task fromDto(TaskDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setTodoDate(dto.getTodoDate());
        task.setPriority(dto.getPriority());

        if (dto.getComments() != null) {
            task.setComments(dto.getComments().stream()
                    .map(TaskMapper::commentFromDto)
                    .collect(Collectors.toList()));
        }

        return task;
    }

    private static Comment commentFromDto(CommentDto dto) {
        Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setAuthor(dto.getAuthor());
        return comment;
    }
}
