package services.impl;

import clients.ApiClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import enums.TaskStatus;
import models.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.TaskService;

import java.util.List;

public class TaskServiceImpl implements TaskService {

    private final ObjectMapper mapper = new ObjectMapper();

    private static final Logger log = LogManager.getLogger(TaskServiceImpl.class);

    public TaskServiceImpl() {
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public List<Task> fetchAllTasksByUserId(Long userId) throws Exception {
        return mapper.readValue(
                ApiClient.get("/tasks/users/%s".formatted(userId)),
                new TypeReference<>() {
                });
    }

    @Override
    public Task fetchByTaskId(Long taskId) throws Exception {
        String response = ApiClient.get("/tasks/%s".formatted(taskId));
        return mapper.readValue(response, Task.class);
    }

    @Override
    public Task createTask(Task newTask) throws Exception {
        String newTaskJson = mapper.writeValueAsString(newTask);
        String response = ApiClient.post("/tasks", newTaskJson);
        return mapper.readValue(response, Task.class);
    }

    @Override
    public Task updateTask(Task updateTask) throws Exception {
        String updateTaskJson = mapper.writeValueAsString(updateTask);
        String response = ApiClient.put("/tasks", updateTaskJson);
        return mapper.readValue(response, Task.class);
    }

    @Override
    public Task changeTaskStatus(Long taskId, TaskStatus taskStatus) {
        return null;
    }

    @Override
    public void deleteTask(Long taskId) throws Exception {
        ApiClient.delete("/tasks/%s".formatted(taskId));
    }
}
