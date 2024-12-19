package factory;

import model.Bug;
import model.Feature;
import model.Improvement;
import model.Task;

public class TaskFactory {
    public static Task createTask(String type, String title, String priority, String assignedTo) {
        switch (type) {
            case "Bug":
                return new Bug(title, priority, assignedTo);
            case "Feature":
                return new Feature(title, priority, assignedTo);
            case "Improvement":
                return new Improvement(title, priority, assignedTo);
            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }
    }
}
