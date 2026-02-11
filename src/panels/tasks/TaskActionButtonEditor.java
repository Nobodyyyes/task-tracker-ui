package panels.tasks;

import models.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import panels.tasks.dialogs.DeleteTaskDialog;
import panels.tasks.dialogs.EditTaskDialog;
import services.TaskService;

import javax.swing.*;
import java.awt.*;

public class TaskActionButtonEditor extends DefaultCellEditor {

    private static final Logger log = LogManager.getLogger(TaskActionButtonEditor.class);

    private JPanel panel = new JPanel();

    private Task currentTask;
    private final JTable table;
    private final TaskService taskService;
    private final TaskPanel taskPanel;

    public TaskActionButtonEditor(JTable table,
                                  TaskService taskService,
                                  TaskPanel taskPanel) {
        super(new JCheckBox());
        this.table = table;
        this.taskService = taskService;
        this.taskPanel = taskPanel;

        JButton btnEdit = new JButton("Изменить");
        JButton btnDelete = new JButton("Удалить");

        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.add(btnEdit);
        panel.add(btnDelete);

        btnEdit.addActionListener(e -> updateTask());
        btnDelete.addActionListener(e -> deleteTask());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {

        try {
            Long taskId = (Long) table.getValueAt(row, 0);
            currentTask = taskService.getByTaskId(taskId);
        } catch (Exception e) {
            log.info("Не знаю что за ошибка...");
        }

        return panel;
    }

    private void updateTask() {
        if (currentTask == null) {
            JOptionPane.showMessageDialog(table,
                    "Не удалось определить задачу для редактирования",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            fireEditingStopped();
        }

        EditTaskDialog dialog = new EditTaskDialog(
                (Frame) SwingUtilities.getWindowAncestor(table),
                taskService,
                currentTask
        );

        dialog.setVisible(true);

        if (dialog.isSaved()) {
            taskPanel.loadTasks();
        }

        fireEditingStopped();
    }

    private void deleteTask() {
        if (currentTask == null) {
            JOptionPane.showMessageDialog(table,
                    "Не удалось определить задачу для редактирования",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            fireEditingStopped();
        }

        DeleteTaskDialog dialog = new DeleteTaskDialog(
                (Frame) SwingUtilities.getWindowAncestor(table),
                currentTask.getTitle()
        );

        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            try {
                taskService.deleteTask(currentTask.getId());
                taskPanel.loadTasks();
            } catch (Exception e) {
                log.info("Не удалось удалить задачу");
            }
        }

        fireEditingStopped();
    }
}
