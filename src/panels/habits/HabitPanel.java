package panels.habits;

import enums.Tag;
import models.Habit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import panels.habits.dialogs.CreateHabitDialog;
import services.HabitService;
import utils.CurrentUser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HabitPanel extends JPanel {

    private static final Logger log = LogManager.getLogger(HabitPanel.class);

    private final DefaultTableModel tableModel;
    private final HabitService habitService;

    private final JComboBox<Tag> tagComboFilter;
    private final JTextField searchField;

    public HabitPanel(HabitService habitService) {
        this.habitService = habitService;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Название",
                        "Описание",
                        "Периодичность",
                        "Дата начала",
                        "Дата конца",
                        "Тэг",
                        "Действие"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };

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

        loadHabits();

        btnCreate.addActionListener(e -> createHabit());
        btnSearch.addActionListener(e -> searchTask());
    }

    private void tableSettings(JTable table) {
        table.getColumn("Действие").setCellRenderer(new HabitActionButtonRenderer());
        table.getColumn("Действие").setCellEditor(new HabitActionButtonEditor(table, habitService, this));
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

        table.getColumn("Периодичность").setCellRenderer(centerRenderer);
        table.getColumn("Дата начала").setCellRenderer(centerRenderer);
        table.getColumn("Дата конца").setCellRenderer(centerRenderer);
        table.getColumn("Тэг").setCellRenderer(centerRenderer);
    }

    public void loadHabits() {
        try {
            List<Habit> habits = habitService.fetchAllHabitsByUserId(CurrentUser.getId());
            tableModel.setRowCount(0);

            for (Habit habit : habits) {
                tableModel.addRow(new Object[]{
                        habit.getId(),
                        habit.getTitle(),
                        habit.getDescription(),
                        habit.getHabitFrequency(),
                        habit.getStartDate(),
                        habit.getEndDate(),
                        habit.getTag(),
                        ""
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка загрузки привычек: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createHabit() {
        CreateHabitDialog createHabitDialog = new CreateHabitDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                habitService
        );

        Habit newHabit = createHabitDialog.showDialog();
        if (newHabit != null) {
            loadHabits();
        }
    }

    private void searchTask() {
        String searchText = searchField.getText().trim().toLowerCase();
        Tag selectedTag = (Tag) tagComboFilter.getSelectedItem();

        List<Habit> habits = new ArrayList<>();
        try {
            habits = habitService.fetchAllHabitsByUserId(CurrentUser.getId());
        } catch (Exception e) {
            log.error("Не удалось получить список задач", e);
        }

        tableModel.setRowCount(0);

        for (Habit habit : habits) {

            if (habit == null) continue;

            boolean matchesTitle = searchText.isEmpty()
                    || habit.getTitle().toLowerCase().contains(searchText);

            boolean matchesTag = selectedTag == null
                    || selectedTag == Tag.DEFAULT
                    || habit.getTag() == selectedTag;

            if (matchesTitle && matchesTag) {
                tableModel.addRow(new Object[]{
                        habit.getId(),
                        habit.getTitle(),
                        habit.getDescription(),
                        habit.getHabitFrequency(),
                        habit.getStartDate(),
                        habit.getEndDate(),
                        habit.getTag(),
                        ""
                });
            }
        }
    }
}
