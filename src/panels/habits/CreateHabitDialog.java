package panels.habits;

import models.Habit;
import services.HabitService;

import javax.swing.*;
import java.awt.*;

public class CreateHabitDialog extends JDialog {

    private Habit createdHabit;

    public CreateHabitDialog(Frame parent, HabitService habitService) {
        super(parent, "Создание привычки", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());


    }
}
