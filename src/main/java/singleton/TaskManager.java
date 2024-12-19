package singleton;

import database.DatabaseManager;
import model.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private static TaskManager instance;

    private TaskManager() {
        DatabaseManager.initialize();
    }

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    // إضافة مهمة
    public void addTask(Task task) {
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO tasks (title, type, priority, assignedTo) VALUES (?, ?, ?, ?)")) {
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getType());
            pstmt.setString(3, task.getPriority());
            pstmt.setString(4, task.getAssignedTo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // استرجاع المهام
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM tasks")) {
            while (rs.next()) {
                tasks.add(new Task(
                        rs.getString("title"),
                        rs.getString("type"),
                        rs.getString("priority"),
                        rs.getString("assignedTo")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // حذف مهمة
    public void deleteTask(String title) {
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM tasks WHERE title = ?")) {
            pstmt.setString(1, title);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
