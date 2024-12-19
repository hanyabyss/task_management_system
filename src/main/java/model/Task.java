package model;

public class Task {
    private String title;
    private String type;
    private String priority;
    private String assignedTo;

    public Task(String title, String type, String priority, String assignedTo) {
        this.title = title;
        this.type = type;
        this.priority = priority;
        this.assignedTo = assignedTo;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getPriority() {
        return priority;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void displayTaskType() {
        System.out.println("Task Type: " + type);
    }
}
