package decorator;

import model.Task;

public abstract class TaskDecorator extends Task {
    protected Task task;

    public TaskDecorator(Task task) {
        super(task.getTitle(), task.getType(), task.getPriority(), task.getAssignedTo());
        this.task = task;
    }
}
