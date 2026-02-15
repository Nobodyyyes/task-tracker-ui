package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import enums.Tag;
import enums.TaskPriority;
import enums.TaskStatus;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {

    private Long id;
    private String title;
    private String description;
    private TaskStatus taskStatus;
    private TaskPriority taskPriority;
    private LocalDate dueDate;
    private Long userId;
    private Tag tag;

    public Task() {
    }

    public Task(Long id, String title, String description, TaskStatus taskStatus, TaskPriority taskPriority, LocalDate dueDate, Long userId, Tag tag) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.taskStatus = taskStatus;
        this.taskPriority = taskPriority;
        this.dueDate = dueDate;
        this.userId = userId;
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskPriority getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(TaskPriority taskPriority) {
        this.taskPriority = taskPriority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}