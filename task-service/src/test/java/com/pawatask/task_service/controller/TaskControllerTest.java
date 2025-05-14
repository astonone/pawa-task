package com.pawatask.task_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawatask.task_service.client.UserClient;
import com.pawatask.task_service.config.TestSecurityConfig;
import com.pawatask.task_service.dto.TaskDto;
import com.pawatask.task_service.dto.UserInfo;
import com.pawatask.task_service.model.Priority;
import com.pawatask.task_service.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @SuppressWarnings("removal")
    @MockBean
    private UserClient userClient;

    private final String token = "Bearer test-token";

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("Viktor K");
        when(userClient.getCurrentUser(anyString())).thenReturn(userInfo);
    }

    @Test
    void getAllTasks_shouldReturnEmptyInitially() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void createTask_shouldReturnCreatedTask() throws Exception {
        TaskDto dto = new TaskDto();
        dto.setTitle("Test task");
        dto.setDescription("Some description");
        dto.setTodoDate(LocalDateTime.now().plusDays(1));
        dto.setPriority(Priority.MEDIUM);

        mockMvc.perform(post("/api/tasks")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test task")))
                .andExpect(jsonPath("$.priority", is("MEDIUM")))
                .andExpect(jsonPath("$.done", is(false)));
    }

    @Test
    void toggleTaskDone_shouldUpdateFlag() throws Exception {
        Long id = createTestTask("Toggle task");

        mockMvc.perform(patch("/api/tasks/" + id + "/toggle"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.done", is(true)));
    }

    @Test
    void getTaskById_shouldReturnCorrectTask() throws Exception {
        Long id = createTestTask("Lookup Task");

        mockMvc.perform(get("/api/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Lookup Task")));
    }

    @Test
    void updateTask_shouldChangeFields() throws Exception {
        Long id = createTestTask("Old title");

        TaskDto updated = new TaskDto();
        updated.setTitle("Updated title");
        updated.setDescription("Updated description");
        updated.setPriority(Priority.CRITICAL);
        updated.setTodoDate(LocalDateTime.now().plusDays(5));

        mockMvc.perform(put("/api/tasks/" + id)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated title")))
                .andExpect(jsonPath("$.priority", is("CRITICAL")));
    }

    private Long createTestTask(String title) throws Exception {
        TaskDto dto = new TaskDto();
        dto.setTitle(title);
        dto.setDescription("Some desc");
        dto.setTodoDate(LocalDateTime.now().plusDays(1));
        dto.setPriority(Priority.LOW);

        ResultActions result = mockMvc.perform(post("/api/tasks")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        String content = result.andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(content).get("id").asLong();
    }
}