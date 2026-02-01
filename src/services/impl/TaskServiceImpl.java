package services.impl;

import clients.ApiClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import enums.TaskStatus;
import models.Task;
import services.TaskService;

import java.util.List;

public class TaskServiceImpl implements TaskService {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<Task> getAllTasksByUserId(Long userId) throws Exception {
        return mapper.readValue(
                ApiClient.get("/tasks/%s".formatted(userId)),
                new TypeReference<List<Task>>() {
        });
    }

    @Override
    public Task getByTaskId(Long taskId) {
        return null;
    }

    @Override
    public Task createTask(Task newTask) throws Exception {
        String newTaskJson = mapper.writeValueAsString(newTask);
        String response = ApiClient.post("/tasks", newTaskJson);
        return mapper.readValue(response, Task.class);
    }

    @Override
    public Task updateTask(Task updateTask) {
        return null;
    }

    @Override
    public Task changeTaskStatus(Long taskId, TaskStatus taskStatus) {
        return null;
    }

    @Override
    public void deleteTask(Long taskId) {

    }
}
