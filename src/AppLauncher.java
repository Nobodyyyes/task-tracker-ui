import panels.LoginDialog;
import panels.MainFrame;
import services.AuthService;
import services.TaskService;
import services.UserService;
import services.impl.AuthServiceImpl;
import services.impl.TaskServiceImpl;
import services.impl.UserServiceImpl;

import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            AuthService authService = new AuthServiceImpl();
            TaskService taskService = new TaskServiceImpl();
            UserService userService = new UserServiceImpl();

            LoginDialog loginDialog = new LoginDialog(null, authService, userService);
            if (loginDialog.showDialog() != null) {
                new MainFrame(taskService).setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}
