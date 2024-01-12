import java.sql.*;

public class TodoListApp {
    // Database connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/todolist";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        try {
            // Establishing a connection to the database
            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            // Creating a statement object
            Statement statement = connection.createStatement();

            // Creating the personal_todos table
            createTable(statement, "personal_todos");

            // Creating the work_todos table
            createTable(statement, "work_todos");

            // Inserting 5 records into personal_todos (Create)
            insertTask(statement, "personal_todos", "Personal Task 1", false);
            insertTask(statement, "personal_todos", "Personal Task 2", true);
            insertTask(statement, "personal_todos", "Personal Task 3", false);
            insertTask(statement, "personal_todos", "Personal Task 4", true);
            insertTask(statement, "personal_todos", "Personal Task 5", false);

            // Inserting 5 records into work_todos (Create)
            insertTask(statement, "work_todos", "Work Task 1", false);
            insertTask(statement, "work_todos", "Work Task 2", true);
            insertTask(statement, "work_todos", "Work Task 3", false);
            insertTask(statement, "work_todos", "Work Task 4", true);
            insertTask(statement, "work_todos", "Work Task 5", false);

            // Displaying tasks from personal_todos (Read)
            displayAllTasks(statement, "personal_todos");

            // Displaying tasks from work_todos (Read)
            displayAllTasks(statement, "work_todos");

            // Updating a task in personal_todos (Update)
            updateTaskCompletionStatus(statement, "personal_todos", 1, true);

            // Displaying updated tasks in personal_todos (Read)
            displayAllTasks(statement, "personal_todos");

            // Deleting a task from work_todos (Delete)
            deleteTask(statement, "work_todos", 2);

            // Displaying tasks after deletion in work_todos (Read)
            displayAllTasks(statement, "work_todos");

            // Closing resources
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Statement statement, String tableName) throws SQLException {
        String createTableSQL = String.format(
                "CREATE TABLE %s (" +
                        "id INT PRIMARY KEY AUTO_INCREMENT," +
                        "task VARCHAR(255) NOT NULL," +
                        "completed BOOLEAN NOT NULL)",
                tableName
        );
        statement.executeUpdate(createTableSQL);
        System.out.println(tableName + " table created successfully.");
    }

    private static void insertTask(Statement statement, String tableName, String task, boolean completed) throws SQLException {
        String insertSQL = String.format(
                "INSERT INTO %s (task, completed) VALUES ('%s', %s)",
                tableName, task, completed
        );
        statement.executeUpdate(insertSQL);
        System.out.println("Task inserted successfully into " + tableName + ".");
    }

    private static void displayAllTasks(Statement statement, String tableName) throws SQLException {
        String selectSQL = String.format("SELECT * FROM %s", tableName);
        ResultSet resultSet = statement.executeQuery(selectSQL);

        System.out.println("Tasks in " + tableName + ":");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String task = resultSet.getString("task");
            boolean completed = resultSet.getBoolean("completed");

            System.out.println(id + ". " + task + " (Completed: " + completed + ")");
        }

        resultSet.close();
    }

    private static void updateTaskCompletionStatus(Statement statement, String tableName, int taskId, boolean completed) throws SQLException {
        String updateSQL = String.format("UPDATE %s SET completed = %s WHERE id = %d", tableName, completed, taskId);
        statement.executeUpdate(updateSQL);
        System.out.println("Task updated successfully in " + tableName + ".");
    }

    private static void deleteTask(Statement statement, String tableName, int taskId) throws SQLException {
        String deleteSQL = String.format("DELETE FROM %s WHERE id = %d", tableName, taskId);
        statement.executeUpdate(deleteSQL);
        System.out.println("Task deleted successfully from " + tableName + ".");
    }
}
