package panels.tasks;

import models.Task;
import services.TaskService;
import utils.CurrentUser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TaskPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final TaskService taskService;

    public TaskPanel(TaskService taskService) {
        this.taskService = taskService;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(
                new String[]{
                        "Название",
                        "Описание",
                        "Статус",
                        "Приоритет",
                        "Дедлайн"
                }, 0
        );

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnCreate = new JButton("Создать");
        JButton btnRefresh = new JButton("Обновить");
        JButton btnDelete = new JButton("Удалить");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnCreate);
        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnDelete);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadTasks();

        btnRefresh.addActionListener(e -> loadTasks());
        btnCreate.addActionListener(e -> createTask());
        btnDelete.addActionListener(e-> deleteTask());
    }

    private void loadTasks() {
        try {
            List<Task> tasks = taskService.getAllTasksByUserId(CurrentUser.getId());
            tableModel.setRowCount(0);

            for (Task t : tasks) {
                tableModel.addRow(new Object[]{
                        t.getTitle(),
                        t.getDescription(),
                        t.getTaskStatus(),
                        t.getTaskPriority(),
                        t.getDueDate()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка загрузки задач: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTask() {
        CreateTaskDialog createDialog = new CreateTaskDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                taskService
        );

        Task newTask = createDialog.showDialog();
        if (newTask != null) {
            loadTasks();
        }
    }

    private void deleteTask() {

    }
}
