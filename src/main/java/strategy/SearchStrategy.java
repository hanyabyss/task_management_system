package strategy;

import model.Task;
import java.util.List;

public interface SearchStrategy {
    List<Task> search(List<Task> tasks, String query);
}
