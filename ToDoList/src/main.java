import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.class.path"));

        String url = "jdbc:sqlite:ToDoList/data/tasks.db";

        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(url)) {
                System.out.println("Connected to the database!");
                createTasksTable(conn);
                startTestProgram(conn);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }

    private static void startTestProgram(Connection conn){
        System.out.println("Welcome to ToDo List Application");
        ArrayList<ToDo> tasks = getData(conn);
        for (ToDo task : tasks) {
            System.out.println(task.toString() + "\n");
        }
        //addTask(tasks, conn);
        int menuOption = 0;
        while (menuOption != 4) {
            System.out.println("Menu Options:");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Remove Task");
            System.out.println("4. Exit");
            Scanner scan = new Scanner(System.in);
            System.out.print("Select an option: ");
            try {
                menuOption = scan.nextInt();
            } catch (Exception e) {
                menuOption = 0;
            }
            
            switch (menuOption) {
                case 1:
                    addTask(tasks, conn);
                    break;
                case 2:
                    for (ToDo task : tasks) {
                        System.out.println(task.toString() + "\n");
                    }
                    break;
                case 3:
                    System.out.print("Enter the title of the task to remove: ");
                    String titleToRemove = scan.next();
                    tasks.removeIf(task -> task.getTitle().equals(titleToRemove));
                    try {
                        deleteTaskFromDatabase(conn, titleToRemove);
                    } catch (SQLException e) {
                        System.out.println("Error deleting task from database: " + e.getMessage());
                    }
                    System.out.println("Task removed if it existed.");
                    break;
                case 4:
                    System.out.println("Exiting application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addTask(ArrayList<ToDo> tasks, Connection conn) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Give Your Task a Title: ");
        String title = scan.nextLine();
        System.out.print("Give Your Task a Description: ");
        String description = scan.nextLine();
        System.out.print("Give Your Task a Due Date: ");
        String dueDate = scan.nextLine();
        ToDo newTask = new ToDo(title, description, dueDate, false);
        tasks.add(newTask);
        try {
            insertTaskIntoDatabase(conn, newTask);
        } catch (SQLException e) {
            System.out.println("Error inserting task into database: " + e.getMessage());
        }
        System.out.println("New Task Added:\n" + newTask.toString());
    }

    private static void createTasksTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Tasks (\n"
                + " id integer PRIMARY KEY,\n"
                + " title text NOT NULL,\n"
                + " description text,\n"
                + " due_date text,\n"
                + " completion_status boolean DEFAULT false\n"
                +");\n";
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    private static void insertTaskIntoDatabase(Connection conn, ToDo task) throws SQLException {
        String sql = "INSERT INTO Tasks(title, description, due_date, completion_status) VALUES(?,?,?,?)";
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getDueDate());
            pstmt.setBoolean(4, task.isCompleted());
            pstmt.executeUpdate();
        }
    }

    private static void deleteTaskFromDatabase(Connection conn, String title) throws SQLException {
        String sql = "DELETE FROM Tasks WHERE title = ?";
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Error deleting task from database: " + e.getMessage());
        }
    }
    private static ArrayList<ToDo> getData(Connection conn){
        ArrayList<ToDo> tasks = new ArrayList<>();
        String sql = "SELECT * FROM Tasks";
        try (var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tasks.add(new ToDo(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("due_date"),
                        rs.getBoolean("completion_status")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving tasks from database: " + e.getMessage());
        }
        return tasks;
    }
}