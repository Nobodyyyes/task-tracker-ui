package panels;

import panels.habits.HabitPanel;
import panels.tasks.TaskPanel;
import services.HabitService;
import services.TaskService;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame(TaskService taskService, HabitService habitService) {
        setTitle("Система управления задачами");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Задачи", new TaskPanel(taskService));
        tabs.addTab("Привычки", new HabitPanel(habitService));

        add(tabs);
    }
}
