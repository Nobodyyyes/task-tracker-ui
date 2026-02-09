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
        super(parent, "Registration", true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField emailField = new JTextField();
        JTextField lastname = new JTextField();
        JTextField firstname = new JTextField();

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Lastname"));
        panel.add(lastname);
        panel.add(new JLabel("Firstname"));
        panel.add(firstname);

        add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton btnRegister = new JButton("Registration");
        buttonPanel.add(btnRegister);
        add(buttonPanel, BorderLayout.SOUTH);

        btnRegister.addActionListener(e -> registerNewUser(
                usernameField,
                passwordField,
                emailField,
                lastname,
                firstname,
                userService));
    }

    private void registerNewUser(JTextField usernameField,
                                 JPasswordField passwordField,
                                 JTextField emailField,
                                 JTextField lastnameField,
                                 JTextField firstnameField,
                                 UserService userService) {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText().trim();
        String lastname = lastnameField.getText().trim();
        String firstname = firstnameField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Requires all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            User user = new User(1L, lastname, firstname, username, email, password, UserStatus.ACTIVE);
            User newUser = userService.register(user);
            CurrentUser.set(newUser);
            registeredUser = newUser;
            JOptionPane.showMessageDialog(this, "Registration success! Welcome");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Registration error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public User showDialog() {
        setVisible(true);
        return registeredUser;
    }
}
