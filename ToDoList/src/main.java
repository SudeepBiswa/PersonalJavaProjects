
import java.sql.Connection;
import java.sql.DriverManager;
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
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Welcome to ToDo List Application");
        ArrayList<ToDo> tasks = new ArrayList<ToDo>();
        addTask(tasks);
        int menuOption = 0;
        while (menuOption != 4) {
            System.out.println("Menu Options:");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Remove Task");
            System.out.println("4. Exit");
            Scanner scan = new Scanner(System.in);
            System.out.print("Select an option: ");
            menuOption = scan.nextInt();
            switch (menuOption) {
                case 1:
                    addTask(tasks);
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

    public static void addTask(ArrayList<ToDo> tasks) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Give Your Task a Title: ");
        String title = scan.nextLine();
        System.out.print("Give Your Task a Description: ");
        String description = scan.nextLine();
        System.out.print("Give Your Task a Due Date: ");
        String dueDate = scan.nextLine();
        ToDo newTask = new ToDo(title, description, dueDate);
        tasks.add(newTask);
        System.out.println("New Task Added:\n" + newTask.toString());
    }
}
