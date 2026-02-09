package models;

import enums.HabitFrequency;

import java.time.LocalDate;

public class Habit {

    private Long id;
    private String title;
    private String description;
    private HabitFrequency habitFrequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
    private Long userId;

    public Habit() {
    }

    public Habit(Long id, String title, String description, HabitFrequency habitFrequency, LocalDate startDate, LocalDate endDate, boolean active, Long userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.habitFrequency = habitFrequency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
        this.userId = userId;
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

    public HabitFrequency getHabitFrequency() {
        return habitFrequency;
    }

    public void setHabitFrequency(HabitFrequency habitFrequency) {
        this.habitFrequency = habitFrequency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
