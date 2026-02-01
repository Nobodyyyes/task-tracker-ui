package panels;

import panels.task.TaskPanel;
import services.TaskService;

import javax.swing.*;

public class MainFrame extends JFrame{

    public MainFrame(TaskService taskService){
        setTitle("Система управления задачами");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Задачи", new TaskPanel(taskService));

        add(tabs);
    }
}
