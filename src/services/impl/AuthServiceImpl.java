package services.impl;

import clients.ApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Auth;
import models.User;
import services.AuthService;

public class AuthServiceImpl implements AuthService {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public User login(String username, String password) throws Exception{
        Auth auth = new Auth(username, password);
        String jsonBody = mapper.writeValueAsString(auth);
        String json = ApiClient.post("/login", jsonBody);
        return mapper.readValue(json, User.class);
    }
}
