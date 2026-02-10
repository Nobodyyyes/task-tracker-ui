package panels;

import models.Habit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import panels.habits.CreateHabitDialog;
import panels.habits.HabitPanel;
import services.HabitService;

import javax.swing.*;
import java.awt.*;

public class ActionButtonEditor extends DefaultCellEditor {

    private static final Logger log = LogManager.getLogger(ActionButtonEditor.class);

    private final JPanel panel = new JPanel();
    private final JButton btnEdit = new JButton("Изменить");
    private final JButton btnDelete = new JButton("Удалить");

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

        }

        return panel;
    }

    private void deleteHabit() {
        try {
            int confirm = JOptionPane.showConfirmDialog(
                    table,
                    "Удалить привычку: " + currentHabit.getTitle() + "?",
                    "Подтверждение",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                habitService.deactivateHabit(currentHabit.getId());
                habitPanel.loadHabits();
            }

            fireEditingStopped();
        } catch (Exception e) {
            log.info("");
        }
    }

    private void updateHabit() {
        CreateHabitDialog dialog = new CreateHabitDialog(
                (Frame) SwingUtilities.getWindowAncestor(table),
                habitService
        );

        Habit updatedHabit = dialog.showDialog();
        if (updatedHabit != null) {
            habitPanel.loadHabits();
        }

        fireEditingStopped();
    }
}
