package panels;

import models.User;
import services.AuthService;
import services.UserService;
import utils.CurrentUser;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {

    private User loggedUser;

    public LoginDialog(Frame parent, AuthService authService, UserService userService) {
        super(parent, "Авторизация", true);
        setSize(400, 220);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton btnLogin = new JButton("Войти");
        JButton btnRegister = new JButton("Регистрация");

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        add(buttonPanel, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> authorization(usernameField, passwordField, authService));
        btnRegister.addActionListener(e -> registration(userService));
    }

    private void authorization(JTextField usernameField, JPasswordField passwordField, AuthService authService) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Введите username и password",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            User user = authService.login(username, password);
            System.out.println("user: " + user);
            CurrentUser.set(user);
            this.loggedUser = user;
            JOptionPane.showMessageDialog(this,
                    "Login successfully!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Authorization error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registration(UserService userService) {
        RegistrationDialog regDialog = new RegistrationDialog(this, userService);
        User user = regDialog.showDialog();
        if (user != null) {
            this.loggedUser = user;
            dispose();
        }
    }

    public User showDialog() {
        setVisible(true);
        return loggedUser;
    }
}
