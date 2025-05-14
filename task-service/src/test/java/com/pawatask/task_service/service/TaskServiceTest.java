package com.pawatask.task_service.service;

import com.pawatask.task_service.client.UserClient;
import com.pawatask.task_service.dto.CommentDto;
import com.pawatask.task_service.dto.TaskDto;
import com.pawatask.task_service.dto.UserInfo;
import com.pawatask.task_service.model.Priority;
import com.pawatask.task_service.model.Task;
import com.pawatask.task_service.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private UserClient userClient;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        userClient = mock(UserClient.class);
        taskService = new TaskService(taskRepository, userClient);
    }

    @Test
    void createTask_shouldSetFieldsAndSave() {
        TaskDto dto = new TaskDto();
        dto.setTitle("Build frontend");
        dto.setDescription("Implement the main layout");
        dto.setPriority(Priority.MEDIUM);
        dto.setTodoDate(LocalDateTime.now().plusDays(1));

        CommentDto comment = new CommentDto();
        comment.setText("Looks good");
        comment.setAuthor("Viktor K");

        dto.setComments(List.of(comment));

        UserInfo user = new UserInfo();
        user.setUsername("Viktor K");
        when(userClient.getCurrentUser("token")).thenReturn(user);

        taskService.createTask(dto, "token");

        verify(taskRepository).save(argThat(task ->
                task.getTitle().equals(dto.getTitle()) &&
                        task.getCreatedBy().equals(user.getUsername()) &&
                        task.getComments().size() == 1
        ));
    }

    @Test
    void updateTask_shouldModifyFieldsAndSave() {
        Task task = Task.builder()
                .id(1L)
                .title("Old title")
                .description("Old description")
                .priority(Priority.LOW)
                .todoDate(LocalDateTime.now().plusDays(2))
                .createdBy("someone")
                .comments(new ArrayList<>())
                .build();

        TaskDto dto = new TaskDto();
        dto.setTitle("New title");
        dto.setDescription("New description");
        dto.setPriority(Priority.HIGH);
        dto.setTodoDate(LocalDateTime.now().plusDays(3));

        UserInfo user = new UserInfo();
        user.setUsername("Irina K");
        when(userClient.getCurrentUser("token")).thenReturn(user);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.updateTask(1L, dto, "token");

        assertEquals("New title", task.getTitle());
        assertEquals("New description", task.getDescription());
        assertEquals(Priority.HIGH, task.getPriority());
        assertEquals("Irina K", task.getLastEditedBy());
        verify(taskRepository).save(task);
    }

    @Test
    void toggleTaskDone_shouldFlipDoneFlag() {
        Task task = Task.builder().id(2L).isDone(false).build();
        when(taskRepository.findById(2L)).thenReturn(Optional.of(task));

        taskService.toggleTaskDone(2L);
        assertTrue(task.isDone());

        taskService.toggleTaskDone(2L);
        assertFalse(task.isDone());

        verify(taskRepository, times(2)).save(task);
    }

    @Test
    void addComment_shouldAppendCommentAndSave() {
        Task task = new Task();
        task.setId(1L);
        task.setComments(new ArrayList<>());

        CommentDto commentDto = new CommentDto();
        commentDto.setText("New comment");

        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("viktor");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userClient.getCurrentUser(anyString())).thenReturn(userInfo);
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0)); // 🔧 фикс

        Task result = taskService.addComment(1L, commentDto, "Bearer token");

        assertThat(result.getComments()).hasSize(1);
        assertThat(result.getComments().get(0).getText()).isEqualTo("New comment");
        assertThat(result.getComments().get(0).getAuthor()).isEqualTo("viktor");
        verify(taskRepository).save(task);
    }

    @Test
    void getTaskById_shouldReturnTask() {
        Task task = Task.builder().id(5L).title("Some task").build();
        when(taskRepository.findById(5L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(5L);
        assertEquals("Some task", result.getTitle());
    }

    @Test
    void createTask_shouldThrowException_ifTodoDateIsInPast() {
        TaskDto dto = new TaskDto();
        dto.setTitle("Outdated task");
        dto.setTodoDate(LocalDateTime.now().minusDays(1));

        when(userClient.getCurrentUser("token")).thenReturn(new UserInfo());

        assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(dto, "token");
        });
    }

    @Test
    void updateTask_shouldThrowException_ifTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        TaskDto dto = new TaskDto();
        dto.setTodoDate(LocalDateTime.now().plusDays(1));

        assertThrows(EntityNotFoundException.class, () -> {
            taskService.updateTask(99L, dto, "token");
        });
    }

    @Test
    void updateTask_shouldThrowException_ifTodoDateIsInPast() {
        TaskDto dto = new TaskDto();
        dto.setTitle("Invalid date");
        dto.setTodoDate(LocalDateTime.now().minusDays(1));

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Old");
        task.setTodoDate(LocalDateTime.now());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userClient.getCurrentUser("token")).thenReturn(new UserInfo());

        assertThrows(IllegalArgumentException.class, () -> {
            taskService.updateTask(1L, dto, "token");
        });
    }

    @Test
    void toggleTaskDone_shouldThrowException_ifTaskNotFound() {
        when(taskRepository.findById(77L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            taskService.toggleTaskDone(77L);
        });
    }

    @Test
    void addComment_shouldThrowException_ifTaskNotFound() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        CommentDto dto = new CommentDto();
        dto.setText("Comment");

        assertThrows(EntityNotFoundException.class, () -> {
            taskService.addComment(999L, dto, "token");
        });
    }

    @Test
    void getTaskById_shouldThrowException_ifNotFound() {
        when(taskRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            taskService.getTaskById(123L);
        });
    }
}