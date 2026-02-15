package panels.tasks;

import enums.Tag;
import models.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import panels.tasks.dialogs.CreateTaskDialog;
import services.TaskService;
import utils.CurrentUser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TaskPanel extends JPanel {

    private static final Logger log = LogManager.getLogger(TaskPanel.class);

    private final DefaultTableModel tableModel;
    private final TaskService taskService;

    private final JComboBox<Tag> tagComboFilter;
    private final JTextField searchField;

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
                        "Тэг",
                        "Действие"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };;

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        tableSettings(table);
        centerSpecificationColumns(table);

        tagComboFilter = new JComboBox<>(Tag.values());
        tagComboFilter.setSelectedItem(Tag.DEFAULT);

        searchField = new JTextField(15);

        JButton btnCreate = new JButton("Создать");
        JButton btnSearch = new JButton("Поиск");

        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel leftTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftTopPanel.add(btnCreate);

        JPanel rightTopPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightTopPanel.add(new JLabel("Название: "));
        rightTopPanel.add(searchField);
        rightTopPanel.add(tagComboFilter);
        rightTopPanel.add(btnSearch);

        topPanel.add(leftTopPanel, BorderLayout.WEST);
        topPanel.add(rightTopPanel, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        loadTasks();

        btnCreate.addActionListener(e -> createTask());
        btnSearch.addActionListener(e -> searchTask());
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

    private void centerSpecificationColumns(JTable table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        table.getColumn("Статус").setCellRenderer(centerRenderer);
        table.getColumn("Приоритет").setCellRenderer(centerRenderer);
        table.getColumn("Дедлайн").setCellRenderer(centerRenderer);
        table.getColumn("Тэг").setCellRenderer(centerRenderer);
    }

    public void loadTasks() {
        try {
            List<Task> tasks = taskService.fetchAllTasksByUserId(CurrentUser.getId());
            tableModel.setRowCount(0);

            for (Task task : tasks) {
                tableModel.addRow(new Object[]{
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getTaskStatus(),
                        task.getTaskPriority(),
                        task.getDueDate(),
                        task.getTag(),
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

    private void searchTask() {
        String searchText = searchField.getText().trim().toLowerCase();
        Tag selectedTag = (Tag) tagComboFilter.getSelectedItem();

        List<Task> tasks = new ArrayList<>();
        try {
            tasks = taskService.fetchAllTasksByUserId(CurrentUser.getId());
        } catch (Exception e) {
            log.error("Не удалось получить список задач", e);
        }

        tableModel.setRowCount(0);

        for (Task task : tasks) {

            if (task == null) continue;

            boolean matchesTitle = searchText.isEmpty()
                    || task.getTitle().toLowerCase().contains(searchText);

            boolean matchesTag = selectedTag == null
                    || selectedTag == Tag.DEFAULT
                    || task.getTag() == selectedTag;

            if (matchesTitle && matchesTag) {
                tableModel.addRow(new Object[]{
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getTaskStatus(),
                        task.getTaskPriority(),
                        task.getDueDate(),
                        task.getTag(),
                        ""
                });
            }
        }
    }
}
