package model;

public class Improvement extends Task {
    public Improvement(String title, String priority, String assignedTo) {
        super(title, "Improvement", priority, assignedTo); // استخدام القيم المناسبة للبناء في Task
    }

    @Override
    public void displayTaskType() {
        System.out.println("Task Type: Improvement");
    }
}
