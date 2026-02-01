package services;

import enums.UserStatus;
import models.User;

import java.util.List;

public interface UserService {

    List<User> getAll() throws Exception;

    User getById(Long id);

    User getByUsername(String username);

    User register(User newUser);

    User updateUser(Long userId, User updateUser);

    void changeUserStatus(Long userId, UserStatus newStatus);
}
