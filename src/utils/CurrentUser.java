package utils;

import models.User;

public class CurrentUser {

    private static User user;

    public static void set(User u) {
        user = u;
    }

    public static User get() {
        return user;
    }

    public static Long getId() {
        return user != null ? user.getId() : null;
    }
}
