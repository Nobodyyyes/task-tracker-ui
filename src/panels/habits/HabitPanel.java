package panels.habits;

import models.Habit;
import panels.habits.dialogs.CreateHabitDialog;
import services.HabitService;
import utils.CurrentUser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HabitPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final HabitService habitService;

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
                        "Действие"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        tableSettings(table);

        JButton btnCreate = new JButton("Создать");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnCreate);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadHabits();

        btnCreate.addActionListener(e -> createHabit());
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

    public void loadHabits() {
        try {
            List<Habit> habits = habitService.getAllHabitsByUserId(CurrentUser.getId());
            tableModel.setRowCount(0);

            for (Habit habit : habits) {
                tableModel.addRow(new Object[]{
                        habit.getId(),
                        habit.getTitle(),
                        habit.getDescription(),
                        habit.getHabitFrequency(),
                        habit.getStartDate(),
                        habit.getEndDate(),
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
}
