package ui;

import factory.TaskFactory;
import model.Task;
import singleton.TaskManager;

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
    private JLabel dynamicNotificationLabel;

    public TaskManagementUI() {
        setTitle("Task Management System");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // ** شريط الإشعارات الديناميكي **
        dynamicNotificationLabel = new JLabel("Welcome!");
        dynamicNotificationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dynamicNotificationLabel.setForeground(Color.WHITE);
        dynamicNotificationLabel.setOpaque(true);
        dynamicNotificationLabel.setBackground(Color.DARK_GRAY);
        dynamicNotificationLabel.setVisible(true); // إظهار شريط الإشعارات دائمًا
        add(dynamicNotificationLabel, BorderLayout.SOUTH);

        // ** جدول عرض المهام **
        String[] columnNames = {"Title", "Type", "Priority", "Assigned To"};
        tableModel = new DefaultTableModel(columnNames, 0);
        taskTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(taskTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // ** لوحة الإدخال **
        JPanel inputPanel = new JPanel(new BorderLayout());

        // لوحة لإدخال الحقول
        JPanel formPanel = new JPanel(new FlowLayout());
        formPanel.add(new JLabel("Task Title:"));
        taskField = new JTextField(15);
        formPanel.add(taskField);

        formPanel.add(new JLabel("Task Type:"));
        JComboBox<String> taskTypeCombo = new JComboBox<>(new String[]{"Bug", "Feature", "Improvement"});
        formPanel.add(taskTypeCombo);

        formPanel.add(new JLabel("Priority:"));
        JComboBox<String> priorityCombo = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        formPanel.add(priorityCombo);

        formPanel.add(new JLabel("Assigned To:"));
        JComboBox<String> userCombo = new JComboBox<>(new String[]{"Admin", "Developer", "Tester"});
        formPanel.add(userCombo);

        inputPanel.add(formPanel, BorderLayout.NORTH);

        // ** لوحة الأزرار **
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Task");
        buttonPanel.add(addButton);

        JButton deleteButton = new JButton("Delete Task");
        buttonPanel.add(deleteButton);

        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ** لوحة البحث والإحصائيات **
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        statsLabel = new JLabel("Tasks Statistics: 0 Total Tasks");

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(statsLabel);

        inputPanel.add(searchPanel, BorderLayout.CENTER);

        // إضافة لوحة الإدخال بأكملها
        add(inputPanel, BorderLayout.NORTH);

        // ** الوظائف **

        // إضافة مهمة جديدة
        addButton.addActionListener((ActionEvent e) -> {
            String title = taskField.getText();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Task title cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String type = (String) taskTypeCombo.getSelectedItem();
            String priority = (String) priorityCombo.getSelectedItem();
            String assignedTo = (String) userCombo.getSelectedItem();

            Task task = TaskFactory.createTask(type, title, priority, assignedTo);
            TaskManager.getInstance().addTask(task);

            showDynamicNotification("Task Added: " + title);
            taskField.setText(""); // مسح حقل الإدخال
            refreshTaskTable(); // تحديث الجدول
        });

        // حذف مهمة
        deleteButton.addActionListener((ActionEvent e) -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a task to delete!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String title = (String) tableModel.getValueAt(selectedRow, 0);
            TaskManager.getInstance().deleteTask(title);

            showDynamicNotification("Task Deleted: " + title);
            refreshTaskTable(); // تحديث الجدول
        });

        // البحث عن المهام
        searchButton.addActionListener((ActionEvent e) -> {
            String query = searchField.getText().toLowerCase();
            filterTaskTable(query);
        });

        // تحديث الجدول عند بدء التطبيق
        refreshTaskTable();
    }

    // عرض إشعار ديناميكي
    private void showDynamicNotification(String message) {
        dynamicNotificationLabel.setText(message);
        dynamicNotificationLabel.setVisible(true);

        Timer timer = new Timer(3000, e -> dynamicNotificationLabel.setVisible(false));
        timer.setRepeats(false);
        timer.start();
    }

    // تحديث جدول المهام
    private void refreshTaskTable() {
        tableModel.setRowCount(0);
        List<Task> tasks = TaskManager.getInstance().getTasks();

        for (Task task : tasks) {
            tableModel.addRow(new Object[]{task.getTitle(), task.getType(), task.getPriority(), task.getAssignedTo()});
        }

        statsLabel.setText("Tasks Statistics: " + tasks.size() + " Total Tasks");
    }

    // تصفية جدول المهام
    private void filterTaskTable(String query) {
        tableModel.setRowCount(0);
        List<Task> tasks = TaskManager.getInstance().getTasks();

        for (Task task : tasks) {
            if (task.getTitle().toLowerCase().contains(query) ||
                task.getType().toLowerCase().contains(query) ||
                task.getPriority().toLowerCase().contains(query) ||
                task.getAssignedTo().toLowerCase().contains(query)) {
                tableModel.addRow(new Object[]{task.getTitle(), task.getType(), task.getPriority(), task.getAssignedTo()});
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new TaskManagementUI().setVisible(true));
    }
}




