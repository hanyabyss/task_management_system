package strategy;

import model.Task;
import java.util.List;
import java.util.stream.Collectors;

public class SearchByTitle implements SearchStrategy {
    @Override
    public List<Task> search(List<Task> tasks, String query) {
        return tasks.stream()
                .filter(task -> task.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}
