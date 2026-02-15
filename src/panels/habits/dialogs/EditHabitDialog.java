package panels.habits.dialogs;

import enums.HabitFrequency;
import enums.Tag;
import models.Habit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.HabitService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;

public class EditHabitDialog extends JDialog {

    private static final Logger log = LogManager.getLogger(EditHabitDialog.class);

    private boolean saved = false;
    private final Habit habit;
    private final HabitService habitService;

    private JTextField titleField;
    private JTextArea descriptionField;
    private JComboBox<HabitFrequency> frequencyComboBox;
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    private JComboBox<Tag> tagCombo;

    public EditHabitDialog(Frame owner, HabitService habitService, Habit habit) {
        super(owner, "Редактирование привычки", true);
        this.habitService = habitService;
        this.habit = habit;

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
        titleField = new JTextField(habit.getTitle());
        descriptionField = new JTextArea(habit.getDescription());

        frequencyComboBox = new JComboBox<>(HabitFrequency.values());
        frequencyComboBox.setSelectedItem(habit.getHabitFrequency());

        startDateSpinner = new JSpinner(new SpinnerDateModel());
        startDateSpinner.setValue(java.util.Date.from(habit.getStartDate()
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));

        endDateSpinner = new JSpinner(new SpinnerDateModel());
        endDateSpinner.setValue(java.util.Date.from(habit.getEndDate()
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));

        tagCombo = new JComboBox<>(Tag.values());
        tagCombo.setSelectedItem(Tag.DEFAULT);
    }

    private JPanel panelSettings(JPanel formPanel) {
        formPanel.add(new JLabel("Название:"));
        formPanel.add(titleField);

        formPanel.add(new JLabel("Описание:"));
        formPanel.add(new JScrollPane(descriptionField));

        formPanel.add(new JLabel("Периодичность:"));
        formPanel.add(frequencyComboBox);

        formPanel.add(new JLabel("Дата начала:"));
        formPanel.add(startDateSpinner);

        formPanel.add(new JLabel("Дата конца:"));
        formPanel.add(endDateSpinner);

        formPanel.add(new JLabel("Тэг"));
        formPanel.add(tagCombo);

        return formPanel;
    }

    private LocalDate modifyToLocalDate(JSpinner dateSpinner) {
        return LocalDate.ofInstant(
                ((java.util.Date) dateSpinner.getValue()).toInstant(),
                java.time.ZoneId.systemDefault());
    }

    private void saveChanges() {
        try {
            HabitFrequency habitFrequency = (HabitFrequency) frequencyComboBox.getSelectedItem();
            Tag tag = (Tag) tagCombo.getSelectedItem();
            LocalDate startDate = modifyToLocalDate(startDateSpinner);
            LocalDate endDate = modifyToLocalDate(endDateSpinner);

            habit.setTitle(titleField.getText());
            habit.setDescription(descriptionField.getText());
            habit.setHabitFrequency(habitFrequency);
            habit.setStartDate(startDate);
            habit.setEndDate(endDate);
            habit.setTag(tag);

            habitService.updateHabit(habit);

            saved = true;
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка при сохранении изменений: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);

            log.info("Не удалось сохранить привычку: {}", e.getMessage());
        }
    }

    public boolean isSaved() {
        return saved;
    }
}
