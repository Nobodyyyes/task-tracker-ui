package services;

import models.Habit;

import java.util.List;

public interface HabitService {

    Habit createHabit(Habit newHabit) throws Exception;

    Habit getById(Long habitId) throws Exception;

    List<Habit> getAllHabitsByUserId(Long userId) throws Exception;

    Habit updateHabit(Habit updateHabit) throws Exception;

    void deactivateHabit(Long habitId) throws Exception;
}
