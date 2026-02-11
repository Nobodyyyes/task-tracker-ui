package panels.habits;

import javax.swing.*;
import java.awt.*;

public class DeleteHabitDialog extends JDialog {

    private boolean confirmed = false;

    public DeleteHabitDialog(Frame owner, String habitTitle) {
        super(owner, "Подтверждение удаления", true);
        setSize(350, 180);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JLabel message = new JLabel(
                "Вы действительно хотите удалить привычку: %s?".formatted(habitTitle),
                SwingConstants.CENTER
        );

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(message, BorderLayout.CENTER);

        JButton btnConfirm = new JButton("Да");
        JButton btnCancel = new JButton("Отмена");

        JPanel buttons = new JPanel();
        buttons.add(btnConfirm);
        buttons.add(btnCancel);

        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        btnConfirm.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        add(centerPanel, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
