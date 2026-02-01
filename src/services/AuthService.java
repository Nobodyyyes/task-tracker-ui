package services;

import models.User;

public interface AuthService {

    User login(String username, String password) throws Exception;
}
