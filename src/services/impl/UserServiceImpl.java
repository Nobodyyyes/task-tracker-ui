package services.impl;

import clients.ApiClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import enums.UserStatus;
import models.User;
import services.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<User> getAll() throws Exception {
        String json = ApiClient.get("/users");
        return mapper.readValue(json, new TypeReference<>() {
        });
    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public User getByUsername(String username) {
        return null;
    }

    @Override
    public User register(User newUser) {
        return null;
    }

    @Override
    public User updateUser(Long userId, User updateUser) {
        return null;
    }

    @Override
    public void changeUserStatus(Long userId, UserStatus newStatus) {

    }
}
