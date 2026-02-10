package services.impl;

import clients.ApiClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import models.Habit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.HabitService;

import java.util.List;

public class HabitServiceImpl implements HabitService {

    private final ObjectMapper mapper = new ObjectMapper();

    private static final Logger log = LogManager.getLogger(HabitServiceImpl.class);

    public HabitServiceImpl() {
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Habit createHabit(Habit newHabit) throws Exception {
        String newHabitJson = mapper.writeValueAsString(newHabit);
        String response = ApiClient.post("/habits", newHabitJson);
        return mapper.readValue(response, Habit.class);
    }

    @Override
    public Habit getById(Long habitId) throws Exception {
        String response = ApiClient.get("/habits/%s".formatted(habitId));
        return mapper.readValue(response, Habit.class);
    }

    @Override
    public List<Habit> getAllHabitsByUserId(Long userId) throws Exception {
        String response = ApiClient.get("/habits/users/%s".formatted(userId));
        log.info("HABITS RESPONSE = {}", response);
        return mapper.readValue(response, new TypeReference<>() {
        });
    }

    @Override
    public Habit updateHabit(Habit updateHabit) throws Exception {
        String updateHabitJson = mapper.writeValueAsString(updateHabit);
        String response = ApiClient.put("/habits", updateHabitJson);
        return mapper.readValue(response, Habit.class);
    }

    @Override
    public void deactivateHabit(Long habitId) throws Exception {
        ApiClient.delete("/habits/deactivate/%s".formatted(habitId));
    }
}
