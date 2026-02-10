package panels;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import panels.habits.HabitPanel;
import panels.tasks.TaskPanel;
import services.HabitService;
import services.TaskService;

import javax.swing.*;

public class MainFrame extends JFrame {

    private static final Logger log = LogManager.getLogger(MainFrame.class);

    public MainFrame(TaskService taskService, HabitService habitService) {
        setTitle("Система управления задачами");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Задачи", new TaskPanel(taskService));
        tabs.addTab("Привычки", new HabitPanel(habitService));

        add(tabs);

        log.info("here");
    }
}
