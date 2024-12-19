package model;

public class Bug extends Task {
    public Bug(String title, String priority, String assignedTo) {
        super(title, "Bug", priority, assignedTo); // استخدام القيم المناسبة للبناء في Task
    }

    @Override
    public void displayTaskType() {
        System.out.println("Task Type: Bug");
    }
}


