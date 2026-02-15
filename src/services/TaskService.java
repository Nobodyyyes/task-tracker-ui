package services;

import enums.TaskStatus;
import models.Task;

import java.util.List;

public interface TaskService {

    List<Task> fetchAllTasksByUserId(Long userId) throws Exception;

    Task fetchByTaskId(Long taskId) throws Exception;

    Task createTask(Task newTask) throws Exception;

    Task updateTask(Task updateTask) throws Exception;

    Task changeTaskStatus(Long taskId, TaskStatus taskStatus);

    void deleteTask(Long taskId) throws Exception;
}
