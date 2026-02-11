package panels.tasks.dialogs;

import enums.TaskPriority;
import enums.TaskStatus;
import models.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.TaskService;
import utils.CurrentUser;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class CreateTaskDialog extends JDialog {

    private static final Logger log = LogManager.getLogger(CreateTaskDialog.class);

    private Task createdTask;

    public CreateTaskDialog(Frame parent, TaskService taskService) {
        super(parent, "Создание задачи", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        JTextField titleField = new JTextField();
        JTextArea descriptionField = new JTextArea(3, 20);

        JComboBox<TaskStatus> statusCombo = new JComboBox<>(TaskStatus.values());
        statusCombo.setSelectedItem(TaskStatus.TODO);

        JComboBox<TaskPriority> priorityCombo = new JComboBox<>(TaskPriority.values());
        priorityCombo.setSelectedItem(TaskPriority.MEDIUM);

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

        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        TaskStatus taskStatus = (TaskStatus) statusCombo.getSelectedItem();
        TaskPriority taskPriority = (TaskPriority) priorityCombo.getSelectedItem();
        LocalDateTime dueDate = LocalDateTime.ofInstant(
                ((java.util.Date) dueDateSpinner.getValue()).toInstant(),
                java.time.ZoneId.systemDefault()
        );

        btnCreate.addActionListener(e -> createTaskProcess(
                title,
                description,
                taskStatus,
                taskPriority,
                dueDate,
                taskService));


    }

    private void createTaskProcess(String title,
                                   String description,
                                   TaskStatus taskStatus,
                                   TaskPriority taskPriority,
                                   LocalDateTime dueDate,
                                   TaskService taskService) {
        try {
            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setTaskStatus(taskStatus);
            task.setTaskPriority(taskPriority);
            task.setDueDate(dueDate);
            task.setUserId(CurrentUser.getId());

            createdTask = taskService.createTask(task);

            JOptionPane.showMessageDialog(this, "Задача создана!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка создания задачи: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            log.info("Ошибка создания задачи: {}", ex.getMessage());
        }
    }

    private void updateTask(String title,
                            String description,
                            TaskStatus taskStatus,
                            TaskPriority taskPriority,
                            LocalDateTime dueDate,
                            TaskService taskService) {
        try {
            if (createdTask == null) {
                JOptionPane.showMessageDialog(this,
                        "Нет задачи для обновления",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Название обязательно!",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            createdTask.setTitle(title);
            createdTask.setDescription(description);
            createdTask.setTaskStatus(taskStatus);
            createdTask.setTaskPriority(taskPriority);
            createdTask.setDueDate(dueDate);

            Task updated = taskService.updateTask(createdTask);
            this.createdTask = updated;

            JOptionPane.showMessageDialog(this, "Задача обновлена!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка обновления задачи: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private void deleteTask() {

    }

    public Task showDialog() {
        setVisible(true);
        return createdTask;
    }
}
