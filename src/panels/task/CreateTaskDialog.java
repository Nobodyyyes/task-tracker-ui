package panels.task;

import enums.TaskPriority;
import enums.TaskStatus;
import models.Task;
import services.TaskService;
import utils.CurrentUser;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class CreateTaskDialog extends JDialog {

    private Task createdTask;

    public CreateTaskDialog(Frame parent, TaskService taskService) {
        super(parent, "Создание задачи", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        JTextField statusField = new JTextField();
        JTextField priorityField = new JTextField();
        JTextField dueDateField = new JTextField();

        JTextField titleField = new JTextField();
        JTextArea descriptionField = new JTextArea(3, 20);

        JComboBox<TaskStatus> statusCombo = new JComboBox<>(TaskStatus.values());
        JComboBox<TaskPriority> priorityCombo = new JComboBox<>(TaskPriority.values());

        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dueDateSpinner = new JSpinner(dateModel);

        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dueDateSpinner, "yyyy-MM-dd");
        dueDateSpinner.setEditor(dateEditor);


        panel.add(new JLabel("Название:"));
        panel.add(titleField);

        panel.add(new JLabel("Описание:"));
        panel.add(new JScrollPane(descriptionField));

        panel.add(new JLabel("Статус:"));
        panel.add(statusCombo);

        panel.add(new JLabel("Приоритет:"));
        panel.add(priorityCombo);

        panel.add(new JLabel("Дедлайн:"));
        panel.add(dueDateSpinner);

        add(panel, BorderLayout.CENTER);

        JButton btnCreate = new JButton("Создать");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnCreate);
        add(bottomPanel, BorderLayout.SOUTH);

        btnCreate.addActionListener(e -> {
            try {
                String title = titleField.getText().trim();
                String description = descriptionField.getText().trim();
                String status = statusField.getText().trim();
                String priority = priorityField.getText().trim();
                String dueDateStr = dueDateField.getText().trim();

                if (title.isEmpty() || status.isEmpty() || priority.isEmpty() || dueDateStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Заполните все обязательные поля", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalDateTime dueDate = LocalDateTime.parse(dueDateStr);

                Long userId = CurrentUser.getId();

                Task task = new Task();
                task.setTitle(title);
                task.setDescription(description);
                task.setTaskStatus(TaskStatus.valueOf(status));
                task.setTaskPriority(TaskPriority.valueOf(priority));
                task.setDueDate(dueDate);
                task.setUserId(userId);

                createdTask = taskService.createTask(task);

                JOptionPane.showMessageDialog(this, "Задача создана!");
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка создания задачи: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public Task showDialog() {
        setVisible(true);
        return createdTask;
    }
}
