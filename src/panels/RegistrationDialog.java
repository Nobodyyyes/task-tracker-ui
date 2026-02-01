package panels;

import enums.UserStatus;
import models.User;
import services.UserService;
import utils.CurrentUser;

import javax.swing.*;
import java.awt.*;

public class RegistrationDialog extends JDialog {

    private User registeredUser;

    public RegistrationDialog(LoginDialog parent, UserService userService) {
        super(parent, "Регистрация", true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField emailField = new JTextField();

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton btnRegister = new JButton("Зарегистрироваться");
        buttonPanel.add(btnRegister);
        add(buttonPanel, BorderLayout.SOUTH);

        btnRegister.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText().trim();

            if(username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Все поля обязательны", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                User user = new User(1L, "test", "test", username, "email", password, UserStatus.ACTIVE);
                User newUser = userService.register(user);
                CurrentUser.set(newUser);
                registeredUser = newUser;
                JOptionPane.showMessageDialog(this, "Регистрация успешна! Добро пожаловать, " + newUser.getUsername());
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка регистрации: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public User showDialog() {
        setVisible(true);
        return registeredUser;
    }
}
