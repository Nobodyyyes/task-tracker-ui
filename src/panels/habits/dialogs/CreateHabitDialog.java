package panels.habits.dialogs;

import enums.HabitFrequency;
import enums.Tag;
import models.Habit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.HabitService;
import utils.CurrentUser;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CreateHabitDialog extends JDialog {

    private static final Logger log = LogManager.getLogger(CreateHabitDialog.class);

    private final HabitService habitService;

    private Habit createdHabit;

    private JTextField titleField;
    private JTextArea descriptionField;
    private JComboBox<HabitFrequency> habitFrequencyCombo;
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    private JComboBox<Tag> tagCombo;

    public CreateHabitDialog(Frame parent, HabitService habitService) {
        super(parent, "Создание привычки", true);
        this.habitService = habitService;

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

        btnCreate.addActionListener(e -> createHabit());
    }

    private void fieldsBuild() {
        titleField = new JTextField();
        descriptionField = new JTextArea(3, 20);

        habitFrequencyCombo = new JComboBox<>(HabitFrequency.values());
        habitFrequencyCombo.setSelectedItem(HabitFrequency.DAILY);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        startDateSpinner = new JSpinner(dateModel);
        endDateSpinner = new JSpinner(dateModel);

        JSpinner.DateEditor startDateEditor = new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd");
        startDateSpinner.setEditor(startDateEditor);

        JSpinner.DateEditor endDateEditor = new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd");
        endDateSpinner.setEditor(endDateEditor);

        tagCombo = new JComboBox<>(Tag.values());
        tagCombo.setSelectedItem(Tag.DEFAULT);
    }

    private void populatePanel(JPanel panel) {
        panel.add(new JLabel("Название"));
        panel.add(titleField);

        panel.add(new JLabel("Описание"));
        panel.add(new JScrollPane(descriptionField));

        panel.add(new JLabel("Периодичность"));
        panel.add(habitFrequencyCombo);

        panel.add(new JLabel("Дата начала"));
        panel.add(startDateSpinner);

        panel.add(new JLabel("Дата конца"));
        panel.add(endDateSpinner);

        panel.add(new JLabel("Тэг"));
        panel.add(tagCombo);
    }

    private void createHabit() {

        String title = titleField.getText();
        String description = descriptionField.getText();
        HabitFrequency habitFrequency = (HabitFrequency) habitFrequencyCombo.getSelectedItem();
        LocalDate startDate = LocalDate.from(LocalDateTime.ofInstant(
                ((java.util.Date) startDateSpinner.getValue()).toInstant(),
                java.time.ZoneId.systemDefault()
        ));

        LocalDate endDate = LocalDate.from(LocalDateTime.ofInstant(
                ((java.util.Date) endDateSpinner.getValue()).toInstant(),
                java.time.ZoneId.systemDefault()
        ));
        Tag tag = (Tag) tagCombo.getSelectedItem();

        try {
            Habit habit = new Habit();
            habit.setTitle(title);
            habit.setDescription(description);
            habit.setHabitFrequency(habitFrequency);
            habit.setStartDate(startDate);
            habit.setEndDate(endDate);
            habit.setUserId(CurrentUser.getId());
            habit.setTag(tag);

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
