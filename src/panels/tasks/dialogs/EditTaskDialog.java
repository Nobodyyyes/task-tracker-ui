package panels.tasks.dialogs;

import enums.Tag;
import enums.TaskPriority;
import enums.TaskStatus;
import models.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.TaskService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class EditTaskDialog extends JDialog {

    private static final Logger log = LogManager.getLogger(EditTaskDialog.class);

    private boolean saved = false;
    private final Task task;
    private final TaskService taskService;

    private JTextField titleTask;
    private JTextArea descriptionTask;
    private JComboBox<TaskStatus> taskStatusCombo;
    private JComboBox<TaskPriority> taskPriorityCombo;
    private JSpinner dueDateSpinner;
    private JComboBox<Tag> tagCombo;

    public EditTaskDialog(Frame owner, TaskService taskService, Task task) {
        super(owner, "Редактирование задачи", true);
        this.taskService = taskService;
        this.task = task;

        setSize(400, 350);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));

        fieldsBuild();

        JButton btnSave = new JButton("Сохранить");
        JButton btnCancel = new JButton("Отмена");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        add(panelSettings(formPanel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> saveChanges());
        btnCancel.addActionListener(e -> dispose());
    }

    private void fieldsBuild() {
        titleTask = new JTextField(task.getTitle());
        descriptionTask = new JTextArea(task.getDescription());

        taskStatusCombo = new JComboBox<>(TaskStatus.values());
        taskStatusCombo.setSelectedItem(task.getTaskStatus());

        taskPriorityCombo = new JComboBox<>(TaskPriority.values());
        taskPriorityCombo.setSelectedItem(task.getTaskPriority());

        dueDateSpinner = new JSpinner(new SpinnerDateModel());
        dueDateSpinner.setValue(java.util.Date.from(task.getDueDate()
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));

        tagCombo = new JComboBox<>(Tag.values());
        tagCombo.setSelectedItem(task.getTag());
    }

    private JPanel panelSettings(JPanel formPanel) {
        formPanel.add(new JLabel("Название:"));
        formPanel.add(titleTask);

        formPanel.add(new JLabel("Описание:"));
        formPanel.add(new JScrollPane(descriptionTask));

        formPanel.add(new JLabel("Статус:"));
        formPanel.add(taskStatusCombo);

        formPanel.add(new JLabel("Приоритет:"));
        formPanel.add(taskPriorityCombo);

        formPanel.add(new JLabel("Дата окончания:"));
        formPanel.add(dueDateSpinner);

        formPanel.add(new JLabel("Тэг"));
        formPanel.add(tagCombo);

        return formPanel;
    }

    private void saveChanges() {
        try {
            LocalDate dueDate = LocalDate.from(LocalDateTime.ofInstant(
                    ((java.util.Date) dueDateSpinner.getValue()).toInstant(),
                    ZoneId.systemDefault()));

            task.setTitle(titleTask.getText());
            task.setDescription(descriptionTask.getText());
            task.setTaskStatus((TaskStatus) taskStatusCombo.getSelectedItem());
            task.setTaskPriority((TaskPriority) taskPriorityCombo.getSelectedItem());
            task.setDueDate(dueDate);
            task.setTag((Tag) tagCombo.getSelectedItem());

            taskService.updateTask(task);

            saved = true;
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка при сохранении изменений: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);

            log.info("Не удалось сохранить задачу: {}", e.getMessage());
        }
    }

    public boolean isSaved() {
        return saved;
    }
}
