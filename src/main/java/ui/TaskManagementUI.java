package ui;

import model.Task;
import observer.DynamicNotification;
import singleton.TaskManager;
import strategy.SearchByPriority;
import strategy.SearchByTitle;
import strategy.SearchStrategy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class TaskManagementUI extends JFrame {
    private DefaultTableModel tableModel;
    private JTable taskTable;
    private JTextField taskField;
    private JTextField searchField;
    private JLabel statsLabel;
    private DynamicNotification dynamicNotification;
    private SearchStrategy searchStrategy;

    public TaskManagementUI() {
        setTitle("Task Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // إعداد التصميم والخلفية
        setLayout(new BorderLayout());
        JPanel backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setBackground(new Color(60, 63, 65)); // لون رمادي غامق
        setContentPane(backgroundPanel);

        // شريط الإشعارات الديناميكي
        dynamicNotification = new DynamicNotification();
        add(dynamicNotification, BorderLayout.SOUTH);

        // ربط الإشعارات بـ TaskManager
        TaskManager.getInstance().getNotifier().addObserver(dynamicNotification);

        // ** إنشاء المكونات **

        // لوحة الإدخال
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 1, 10, 10));
        inputPanel.setBackground(new Color(43, 43, 43)); // لون رمادي أفتح
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // صف الإدخال
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.setBackground(new Color(43, 43, 43));
        taskField = new JTextField();
        JComboBox<String> taskTypeCombo = new JComboBox<>(new String[]{"Bug", "Feature", "Improvement"});
        JComboBox<String> priorityCombo = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        JComboBox<String> userCombo = new JComboBox<>(new String[]{"Admin", "Developer", "Tester"});

        formPanel.add(createLabel("Task Title:"));
        formPanel.add(taskField);
        formPanel.add(createLabel("Task Type:"));
        formPanel.add(taskTypeCombo);
        formPanel.add(createLabel("Priority:"));
        formPanel.add(priorityCombo);
        formPanel.add(createLabel("Assigned To:"));
        formPanel.add(userCombo);

        inputPanel.add(formPanel);

        // أزرار الإدخال
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(43, 43, 43));
        JButton addButton = new JButton("Add Task");
        JButton deleteButton = new JButton("Delete Task");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        inputPanel.add(buttonPanel);

        // لوحة البحث
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setBackground(new Color(43, 43, 43));
        searchField = new JTextField(20);
        JButton searchByTitleButton = new JButton("Search by Title");
        JButton searchByPriorityButton = new JButton("Search by Priority");
        statsLabel = createLabel("Tasks Statistics: 0 Total Tasks");

        searchPanel.add(createLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchByTitleButton);
        searchPanel.add(searchByPriorityButton);
        searchPanel.add(statsLabel);

        // إضافة لوحة الإدخال والبحث في الأعلى
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(new Color(43, 43, 43));
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // جدول عرض المهام
        String[] columnNames = {"Title", "Type", "Priority", "Assigned To"};
        tableModel = new DefaultTableModel(columnNames, 0);
        taskTable = new JTable(tableModel);
        taskTable.setBackground(new Color(69, 73, 74));
        taskTable.setForeground(Color.WHITE);
        taskTable.setFillsViewportHeight(true);
        JScrollPane tableScrollPane = new JScrollPane(taskTable);
        tableScrollPane.getViewport().setBackground(new Color(69, 73, 74));
        add(tableScrollPane, BorderLayout.CENTER);

        // ** الوظائف **

        // وظيفة إضافة مهمة
        addButton.addActionListener((ActionEvent e) -> {
            String title = taskField.getText();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Task title cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String type = (String) taskTypeCombo.getSelectedItem();
            String priority = (String) priorityCombo.getSelectedItem();
            String assignedTo = (String) userCombo.getSelectedItem();

            Task task = new Task(title, type, priority, assignedTo);
            TaskManager.getInstance().addTask(task);
            dynamicNotification.update("Task Added: " + task.getTitle());
            taskField.setText(""); // مسح حقل الإدخال
            refreshTaskTable();
        });

        // وظيفة حذف مهمة
        deleteButton.addActionListener((ActionEvent e) -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a task to delete!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String title = (String) tableModel.getValueAt(selectedRow, 0);
            TaskManager.getInstance().deleteTask(title);
            dynamicNotification.update("Task Deleted: " + title);
            refreshTaskTable();
        });

        // وظيفة البحث
        searchByTitleButton.addActionListener((ActionEvent e) -> {
            searchStrategy = new SearchByTitle();
            filterTaskTable(searchField.getText());
        });

        searchByPriorityButton.addActionListener((ActionEvent e) -> {
            searchStrategy = new SearchByPriority();
            filterTaskTable(searchField.getText());
        });

        refreshTaskTable();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }

    private void filterTaskTable(String query) {
        if (searchStrategy == null) return;

        List<Task> filteredTasks = searchStrategy.search(TaskManager.getInstance().getTasks(), query);
        updateTaskTable(filteredTasks);
    }

    private void refreshTaskTable() {
        List<Task> tasks = TaskManager.getInstance().getTasks();
        updateTaskTable(tasks);
        statsLabel.setText("Tasks Statistics: " + tasks.size() + " Total Tasks");
    }

    private void updateTaskTable(List<Task> tasks) {
        tableModel.setRowCount(0);
        for (Task task : tasks) {
            tableModel.addRow(new Object[]{
                task.getTitle(),
                task.getType(),
                task.getPriority(),
                task.getAssignedTo()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TaskManagementUI().setVisible(true));
    }
}
