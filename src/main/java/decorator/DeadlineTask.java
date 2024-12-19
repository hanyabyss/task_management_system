package decorator;

import model.Task;

public class DeadlineTask extends TaskDecorator {
    private String deadline;

    public DeadlineTask(Task task, String deadline) {
        super(task);
        this.deadline = deadline;
    }

    public String getDeadline() {
        return deadline;
    }
}
