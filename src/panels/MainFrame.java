package panels;

import models.User;
import services.UserService;
import services.impl.UserServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame{

    private final UserService userService;
    private final DefaultTableModel tableModel;

    public MainFrame(UserService userService) {
        this.userService = userService;
        setTitle("Users App");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email"}, 0);
        JTable table = new JTable(tableModel);

        JButton btnLoad = new JButton("Загрузить пользователей");

        btnLoad.addActionListener(e -> loadUsers());

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnLoad, BorderLayout.SOUTH);
    }

    private void loadUsers() {
        try {
            List<User> users = userService.getAll();
            tableModel.setRowCount(0); // очистка таблицы

            for (User u : users) {
                tableModel.addRow(new Object[]{});
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        SwingUtilities.invokeLater(() -> new MainFrame(userService).setVisible(true));
    }
}
