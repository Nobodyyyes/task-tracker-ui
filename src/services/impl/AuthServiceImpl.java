package services.impl;

import clients.ApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.User;
import services.AuthService;

public class AuthServiceImpl implements AuthService {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public User login(String username, String password) throws Exception {
        String json = ApiClient.get("/login/username/%s/password/%s".formatted(username, password));
        return mapper.readValue(json, User.class);
    }
}
