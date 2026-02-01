package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import enums.TaskStatus;
import models.Task;

import java.util.List;

public interface TaskService {

    List<Task> getAllTasksByUserId(Long userId) throws Exception;

    Task getByTaskId(Long taskId);

    Task createTask(Task newTask) throws Exception;

    Task updateTask(Task updateTask);

    Task changeTaskStatus(Long taskId, TaskStatus taskStatus);

    void deleteTask(Long taskId);
}
