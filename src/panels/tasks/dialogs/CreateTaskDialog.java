package panels.tasks.dialogs;

import enums.Tag;
import enums.TaskPriority;
import enums.TaskStatus;
import models.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.TaskService;
import utils.CurrentUser;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CreateTaskDialog extends JDialog {

    private static final Logger log = LogManager.getLogger(CreateTaskDialog.class);

    private Task createdTask;

    private JTextField titleField;
    private JTextArea descriptionField;
    private JComboBox<TaskStatus> statusCombo;
    private JComboBox<TaskPriority> priorityCombo;
    private JSpinner dueDateSpinner;
    private JComboBox<Tag> tagCombo;

    public CreateTaskDialog(Frame parent, TaskService taskService) {
        super(parent, "Создание задачи", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        fieldsBuild();
        populatePanel(panel);

        JButton btnCreate = new JButton("Создать");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnCreate);

        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        btnCreate.addActionListener(e -> createTaskProcess(
                titleField,
                descriptionField,
                statusCombo,
                priorityCombo,
                dueDateSpinner,
                taskService));
    }

    private void fieldsBuild() {
        titleField = new JTextField();
        descriptionField = new JTextArea(3, 20);

        statusCombo = new JComboBox<>(TaskStatus.values());
        statusCombo.setSelectedItem(TaskStatus.TODO);

        priorityCombo = new JComboBox<>(TaskPriority.values());
        priorityCombo.setSelectedItem(TaskPriority.MEDIUM);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        dueDateSpinner = new JSpinner(dateModel);

        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dueDateSpinner, "yyyy-MM-dd");
        dueDateSpinner.setEditor(dateEditor);

        tagCombo = new JComboBox<>(Tag.values());
        tagCombo.setSelectedItem(Tag.DEFAULT);
    }

    private void populatePanel(JPanel panel) {
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

        panel.add(new JLabel("Тэг"));
        panel.add(tagCombo);
    }

    private void createTaskProcess(JTextField titleField,
                                   JTextArea descriptionField,
                                   JComboBox<TaskStatus> statusCombo,
                                   JComboBox<TaskPriority> priorityCombo,
                                   JSpinner dueDateSpinner,
                                   TaskService taskService) {

        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        TaskStatus taskStatus = (TaskStatus) statusCombo.getSelectedItem();
        TaskPriority taskPriority = (TaskPriority) priorityCombo.getSelectedItem();
        LocalDate dueDate = LocalDate.from(LocalDateTime.ofInstant(
                ((java.util.Date) dueDateSpinner.getValue()).toInstant(),
                java.time.ZoneId.systemDefault()
        ));
        Tag tag = (Tag) tagCombo.getSelectedItem();

        try {
            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setTaskStatus(taskStatus);
            task.setTaskPriority(taskPriority);
            task.setDueDate(dueDate);
            task.setUserId(CurrentUser.getId());
            task.setTag(tag);

            createdTask = taskService.createTask(task);

            JOptionPane.showMessageDialog(this, "Задача создана!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка создания задачи: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            log.info("Ошибка создания задачи: {}", ex.getMessage());
        }
    }

    public Task showDialog() {
        setVisible(true);
        return createdTask;
    }
}
