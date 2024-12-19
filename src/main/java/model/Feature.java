package model;

public class Feature extends Task {
    public Feature(String title, String priority, String assignedTo) {
        super(title, "Feature", priority, assignedTo); // استخدام القيم المناسبة للبناء في Task
    }

    @Override
    public void displayTaskType() {
        System.out.println("Task Type: Feature");
    }
}


