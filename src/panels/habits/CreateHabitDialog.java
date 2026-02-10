package panels.habits;

import enums.HabitFrequency;
import models.Habit;
import services.HabitService;
import utils.CurrentUser;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CreateHabitDialog extends JDialog {

    private Habit createdHabit;

    public CreateHabitDialog(Frame parent, HabitService habitService) {
        super(parent, "Создание привычки", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        JTextField titleField = new JTextField();
        JTextArea descriptionField = new JTextArea(3, 20);

        JComboBox<HabitFrequency> habitFrequencyCombo = new JComboBox<>(HabitFrequency.values());
        habitFrequencyCombo.setSelectedItem(HabitFrequency.DAILY);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner startDateSpinner = new JSpinner(dateModel);

        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd");
        startDateSpinner.setEditor(dateEditor);

        panel.add(new JLabel("Название"));
        panel.add(titleField);

        panel.add(new JLabel("Описание"));
        panel.add(new JScrollPane(descriptionField));

        panel.add(new JLabel("Периодичность"));
        panel.add(habitFrequencyCombo);

        panel.add(new JLabel("Дата начала"));
        panel.add(startDateSpinner);

        JButton btnCreate = new JButton("Создать");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnCreate);

        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        HabitFrequency habitFrequency = (HabitFrequency) habitFrequencyCombo.getSelectedItem();
        LocalDateTime startDate = LocalDateTime.ofInstant(
                ((java.util.Date) startDateSpinner.getValue()).toInstant(),
                java.time.ZoneId.systemDefault()
        );

        btnCreate.addActionListener(e -> createHabit(
                title,
                description,
                habitFrequency,
                startDate.toLocalDate(),
                LocalDate.now(),
                habitService));
    }

    private void createHabit(String title,
                             String description,
                             HabitFrequency habitFrequency,
                             LocalDate startDate,
                             LocalDate endDate,
                             HabitService habitService) {

        try {
            Habit habit = new Habit();
            habit.setTitle(title);
            habit.setDescription(description);
            habit.setHabitFrequency(habitFrequency);
            habit.setStartDate(startDate);
            habit.setEndDate(endDate);
            habit.setUserId(CurrentUser.getId());

            createdHabit = habitService.createHabit(habit);

            JOptionPane.showMessageDialog(this, "Привычка создана!");
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка создания привычки: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Habit showDialog() {
        setVisible(true);
        return createdHabit;
    }
}
