package panels.tasks;

import models.Task;
import panels.tasks.dialogs.CreateTaskDialog;
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
                        "ID",
                        "Название",
                        "Описание",
                        "Статус",
                        "Приоритет",
                        "Дедлайн",
                        "Действие"
                }, 0
        );

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        tableSettings(table);

        JButton btnCreate = new JButton("Создать");
        JButton btnRefresh = new JButton("Обновить");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnCreate);
        bottomPanel.add(btnRefresh);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadTasks();

        btnRefresh.addActionListener(e -> loadTasks());
        btnCreate.addActionListener(e -> createTask());
    }

    private void tableSettings(JTable table) {
        table.getColumn("Действие").setCellRenderer(new TaskActionButtonRenderer());
        table.getColumn("Действие").setCellEditor(new TaskActionButtonEditor(table, taskService, this));
        table.getColumn("Действие").setPreferredWidth(220);
        table.getColumn("Действие").setMinWidth(180);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);
        table.setRowHeight(30);
    }

    public void loadTasks() {
        try {
            List<Task> tasks = taskService.getAllTasksByUserId(CurrentUser.getId());
            tableModel.setRowCount(0);

            for (Task task : tasks) {
                tableModel.addRow(new Object[]{
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getTaskStatus(),
                        task.getTaskPriority(),
                        task.getDueDate(),
                        ""
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
}
