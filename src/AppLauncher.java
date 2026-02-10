import com.fasterxml.jackson.databind.Module;
import panels.LoginDialog;
import panels.MainFrame;
import services.AuthService;
import services.HabitService;
import services.TaskService;
import services.UserService;
import services.impl.AuthServiceImpl;
import services.impl.HabitServiceImpl;
import services.impl.TaskServiceImpl;
import services.impl.UserServiceImpl;

import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            AuthService authService = new AuthServiceImpl();
            TaskService taskService = new TaskServiceImpl();
            UserService userService = new UserServiceImpl();
            HabitService habitService = new HabitServiceImpl();

            LoginDialog loginDialog = new LoginDialog(null, authService, userService);
            if (loginDialog.showDialog() != null) {
                new MainFrame(taskService, habitService)
                        .setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}
