package panels;

import models.Habit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import panels.habits.DeleteHabitDialog;
import panels.habits.EditHabitDialog;
import panels.habits.HabitPanel;
import services.HabitService;

import javax.swing.*;
import java.awt.*;

public class ActionButtonEditor extends DefaultCellEditor {

    private static final Logger log = LogManager.getLogger(ActionButtonEditor.class);

    private final JPanel panel = new JPanel();

    private Habit currentHabit;
    private final JTable table;
    private final HabitService habitService;
    private final HabitPanel habitPanel;

    public ActionButtonEditor(JTable table,
                              HabitService habitService,
                              HabitPanel habitPanel) {
        super(new JCheckBox());
        this.table = table;
        this.habitService = habitService;
        this.habitPanel = habitPanel;

        JButton btnEdit = new JButton("Изменить");
        JButton btnDelete = new JButton("Удалить");

        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.add(btnEdit);
        panel.add(btnDelete);

        btnEdit.addActionListener(e -> updateHabit());
        btnDelete.addActionListener(e -> deleteHabit());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {

        try {
            Long habitId = (Long) table.getValueAt(row, 0);
            currentHabit = habitService.getById(habitId);
        } catch (Exception e) {
            log.info("Не знаю что за ошибка...");
        }

        return panel;
    }

    private void updateHabit() {
        if (currentHabit == null) {
            JOptionPane.showMessageDialog(table,
                    "Не удалось определить привычку для редактирования",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            fireEditingStopped();
            return;
        }

        EditHabitDialog dialog = new EditHabitDialog(
                (Frame) SwingUtilities.getWindowAncestor(table),
                habitService,
                currentHabit
        );

        dialog.setVisible(true);

        if (dialog.isSaved()) {
            habitPanel.loadHabits();
        }

        fireEditingStopped();
    }

    private void deleteHabit() {
        if (currentHabit == null) {
            JOptionPane.showMessageDialog(table,
                    "Не удалось определить привычку для удаления",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            fireEditingStopped();
        }

        DeleteHabitDialog dialog = new DeleteHabitDialog(
                (Frame) SwingUtilities.getWindowAncestor(table),
                currentHabit.getTitle()
        );

        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            try {
                habitService.deactivateHabit(currentHabit.getId());
                habitPanel.loadHabits();
            } catch (Exception e) {
                log.info("Не удалось удалить привычку");
            }
        }

        fireEditingStopped();
    }
}
