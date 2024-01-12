import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TodoListSwingApp extends JFrame {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/todolist";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private JTextArea tasksTextArea;

    public TodoListSwingApp() {
        super("Todo List Swing App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Create components
        JButton personalButton = new JButton("Personal Todos");
        JButton workButton = new JButton("Work Todos");
        tasksTextArea = new JTextArea(10, 30);

        // Add components to the frame
        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(personalButton);
        buttonPanel.add(workButton);
        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(tasksTextArea), BorderLayout.CENTER);

        // Handle button clicks
        personalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTasks("personal_todos");
            }
        });

        workButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTasks("work_todos");
            }
        });

        // Display initial tasks (from personal_todos)
        refreshTasks("personal_todos");

        setVisible(true);
    }

    private void refreshTasks(String tableName) {
        // Retrieve tasks from the specified table and update tasksTextArea
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            String selectSQL = String.format("SELECT * FROM %s", tableName);
            ResultSet resultSet = statement.executeQuery(selectSQL);

            StringBuilder tasks = new StringBuilder();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String task = resultSet.getString("task");
                boolean completed = resultSet.getBoolean("completed");

                tasks.append(id).append(". ").append(task).append(" (Completed: ").append(completed).append(")\n");
            }

            tasksTextArea.setText(tasks.toString()); // Update text area
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TodoListSwingApp());
    }
}
