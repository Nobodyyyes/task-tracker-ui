package panels.habits;

import models.Habit;
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
                new String[] {
                        "Название",
                        "Описание",
                        "Периодичьность",
                        "Дата начала",
                        "Дата конца"
                }, 0
        );

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnCreate = new JButton("Создать");
        JButton btnUpdate = new JButton("Обновить");
        JButton btnDelete = new JButton("Удалить");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnCreate);
        bottomPanel.add(btnUpdate);
        bottomPanel.add(btnDelete);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadHabits();

        btnCreate.addActionListener(e -> createHabit());
        btnUpdate.addActionListener(e -> updateHabit());
        btnDelete.addActionListener(e -> deleteHabit());
    }

    private void loadHabits() {
        try {
            List<Habit> habits = habitService.getAllHabitsByUserId(CurrentUser.getId());
            tableModel.setRowCount(0);

            for (Habit habit : habits) {
                tableModel.addRow(new Object[]{
                        habit.getTitle(),
                        habit.getDescription(),
                        habit.getHabitFrequency(),
                        habit.getStartDate(),
                        habit.getEndDate()
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

    }

    private void updateHabit() {

    }

    private void deleteHabit() {

    }
}
