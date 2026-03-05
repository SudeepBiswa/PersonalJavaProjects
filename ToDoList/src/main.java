import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class main {
    // shared database connection for entire session
    private static Connection conn;

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.class.path"));

        String url = "jdbc:sqlite:ToDoList/data/tasks.db";

        JFrame frame = new JFrame();
        frame.setTitle("ToDo List Application");
        frame.setSize(400, 300);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setVisible(true);

        // JButton button = new JButton("Click Me");
        // button.setBounds(150, 150, 100, 50);
        // frame.add(button);
        JPanel cards = new JPanel(new CardLayout());
        

        frame.add(cards);
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            System.out.println("Connected to the database!");

            createTasksTable();
            ArrayList<ToDo> tasks = getData();

            homePage(cards, tasks);
            addTasksPage(cards, tasks);

            frame.getContentPane().setBackground(Color.BLACK);
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    closeConnection();
                }
            });

            frame.setVisible(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            closeConnection();
        }

    }

    private static void startTestProgram() { // unused in current UI
        // kept signature without connection; use shared conn if needed
        ArrayList<ToDo> tasks = getData();

        System.out.println("Welcome to ToDo List Application");
        for (ToDo task : tasks) {
            System.out.println(task.toString() + "\n");
        }
        // addTask(tasks); // console helper
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
                    try {
                        deleteTaskFromDatabase(titleToRemove);
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

    private static void homePage(JPanel cards, ArrayList<ToDo> tasks) {
        JPanel homePanel = new JPanel();
        cards.add(homePanel, "home");
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        //cards.add(mainPanel, "home");
        JPanel scrollViewpanel = new JPanel();
        scrollViewpanel.setLayout(new BoxLayout(scrollViewpanel, BoxLayout.Y_AXIS));
        // frame.add(panel);

        for (ToDo task : tasks) {

            JPanel vPanel = new JPanel();
            vPanel.setLayout(new GridLayout(1, 4, 5, 0));
            vPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            vPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
            vPanel.setPreferredSize(new Dimension(380, 36));
            // Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
            // vPanel.setBorder(border);

            JLabel title = new JLabel(task.getTitle());
            title.setHorizontalAlignment(SwingConstants.LEFT);
            //title.setForeground(Color.WHITE);
            //JLabel description = new JLabel(task.getDescription());

            JLabel hyperlink = new JLabel("View Description");
            hyperlink.setForeground(Color.BLUE.darker()); // Set text color to blue
            hyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor to a hand icon on hover
            
            hyperlink.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    viewDescriptionPage(cards, task, tasks);
                    CardLayout cl = (CardLayout) cards.getLayout();
                    cl.show(cards, "view_description");
                }
            });

            //description.setForeground(Color.WHITE);
            JLabel dueDate = new JLabel(task.getDueDate());
            dueDate.setHorizontalAlignment(SwingConstants.LEFT);
            //dueDate.setForeground(Color.WHITE);
            JLabel completionStatus = new JLabel(task.isCompleted() ? "Completed" : "Not Completed");
            completionStatus.setHorizontalAlignment(SwingConstants.LEFT);
            //completionStatus.setForeground(Color.WHITE);

            vPanel.add(title);
            //vPanel.add(description);
            vPanel.add(hyperlink);
            vPanel.add(dueDate);
            vPanel.add(completionStatus);
            scrollViewpanel.add(vPanel);

            //JPanel separator = new JPanel();
            //separator.setPreferredSize(new java.awt.Dimension(399, 1));
            //separator.setBackground(Color.BLACK);
            //scrollViewpanel.add(separator);
            //vPanel.setBackground(Color.BLACK);

            //vPanel.setBackground(Color.BLACK);
            //vPanel.setBorder(BorderFactory.createLineBorder(Color.white, 1));
            //vPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 20");
            vPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

        }

        JScrollPane scrollPane = new JScrollPane(scrollViewpanel);
        // Increase scrolling speed
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        homePanel.add(scrollPane);

        // Add vertical glue to push the button slightly above the bottom
        homePanel.add(Box.createVerticalGlue());

        // Add Task button at the bottom center
        JPanel buttonPanel = new JPanel();
        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cards.getLayout();
            cl.show(cards, "add_tasks");
        });
        buttonPanel.add(addTaskButton);
        homePanel.add(buttonPanel);

    }

    private static void addTasksPage(JPanel cards, ArrayList<ToDo> tasks) {
        JPanel addTasksPanel = new JPanel();
        cards.add(addTasksPanel, "add_tasks");
        addTasksPanel.setLayout(new BoxLayout(addTasksPanel, BoxLayout.Y_AXIS));

        
        JPanel titlePanel = new JPanel();
        JLabel addTasksLabel = new JLabel("Add New Task");
        addTasksLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(addTasksLabel);
        addTasksPanel.add(titlePanel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setPreferredSize(new Dimension(380, 180));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("Title:");
        JLabel descriptionLabel = new JLabel("Description:");
        JLabel dueDateLabel = new JLabel("Due Date (MM/dd/yyyy):");

        JTextField titleField = new JTextField();

        JTextArea descriptionField = new JTextArea(5, 20);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionField);
        descScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        descScroll.setPreferredSize(new Dimension(200, 100));

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        JFormattedTextField dueDateField = new JFormattedTextField(dateFormat);
        dueDateField.setValue(new Date());

        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        descriptionLabel.setHorizontalAlignment(SwingConstants.LEFT);
        dueDateLabel.setHorizontalAlignment(SwingConstants.LEFT);

        titleField.setHorizontalAlignment(SwingConstants.LEFT);
        dueDateField.setHorizontalAlignment(SwingConstants.LEFT);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            //System.out.println("Submit Button Clicked!");
            String title = titleField.getText();
            String description = descriptionField.getText();
            String dueDate = dueDateField.getText();
            ToDo newTask = new ToDo(title, description, dueDate, false);
            tasks.add(newTask);
            try {
                insertTaskIntoDatabase(newTask);
                System.out.println("Task inserted into database successfully!");
            } catch (SQLException ex) {
                System.out.println("Error inserting task into database: " + ex.getMessage());
            }
            System.out.println("New Task Added:\n" + newTask.toString());
            titleField.setText("");
            descriptionField.setText("");
            dueDateField.setValue(new Date());
            // Refresh the home page with updated tasks
            homePage(cards, tasks);
            CardLayout cl = (CardLayout) cards.getLayout();
            cl.show(cards, "home");
        });

        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        formPanel.add(titleLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(descScroll, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 0;
        formPanel.add(dueDateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(dueDateField, gbc);

        addTasksPanel.add(formScroll);

        // Add buttons at the bottom
        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cards.getLayout();
            cl.show(cards, "home");
        });
        buttonPanel.add(backButton);
        buttonPanel.add(submitButton);
        addTasksPanel.add(buttonPanel);
    }

    private static void viewDescriptionPage(JPanel cards, ToDo task, ArrayList<ToDo> tasks) {
        JPanel viewPanel = new JPanel();
        cards.add(viewPanel, "view_description");
        viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));

        // Title panel with remove button
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Task Details");
        titlePanel.add(titleLabel, BorderLayout.WEST);

        JButton removeButton = new JButton("Remove Task");
        removeButton.addActionListener(e -> {
            try {
                deleteTaskFromDatabase(task.getId());
                tasks.remove(task);
                homePage(cards, tasks);
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.show(cards, "home");
            } catch (SQLException ex) {
                System.out.println("Error deleting task: " + ex.getMessage());
            }
        });
        titlePanel.add(removeButton, BorderLayout.EAST);
        viewPanel.add(titlePanel);

        // Task information
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JPanel titleP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleP.add(new JLabel("Title: "));
        titleP.add(new JLabel(task.getTitle()));
        infoPanel.add(titleP);

        JPanel descP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        descP.add(new JLabel("Description: "));
        JTextArea descArea = new JTextArea(task.getDescription(), 5, 20);
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(300, 100));
        descP.add(descScroll);
        infoPanel.add(descP);

        JPanel dateP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dateP.add(new JLabel("Due Date: "));
        dateP.add(new JLabel(task.getDueDate()));
        infoPanel.add(dateP);

        JPanel statusP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusP.add(new JLabel("Status: "));
        JRadioButton completedRadio = new JRadioButton("Completed");
        JRadioButton notCompletedRadio = new JRadioButton("Not Completed");
        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(completedRadio);
        statusGroup.add(notCompletedRadio);
        if (task.isCompleted()) {
            completedRadio.setSelected(true);
        } else {
            notCompletedRadio.setSelected(true);
        }
        completedRadio.addActionListener(e -> {
            task.setCompleted(true);
            try {
                updateTaskStatusInDatabase(task);
                System.out.println("Task status updated to Completed");
            } catch (SQLException ex) {
                System.out.println("Error updating task status: " + ex.getMessage());
            }
        });
        notCompletedRadio.addActionListener(e -> {
            task.setCompleted(false);
            try {
                updateTaskStatusInDatabase(task);
                System.out.println("Task status updated to Not Completed");
            } catch (SQLException ex) {
                System.out.println("Error updating task status: " + ex.getMessage());
            }
        });
        statusP.add(completedRadio);
        statusP.add(notCompletedRadio);
        infoPanel.add(statusP);

        viewPanel.add(infoPanel);

        // Back button at the bottom
        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cards.getLayout();
            cl.show(cards, "home");
        });
        buttonPanel.add(backButton);
        viewPanel.add(buttonPanel);
    }

    private static void addTask(ArrayList<ToDo> tasks) {
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
            insertTaskIntoDatabase(newTask);
        } catch (SQLException e) {
            System.out.println("Error inserting task into database: " + e.getMessage());
        }
        System.out.println("New Task Added:\n" + newTask.toString());
    }

    private static void createTasksTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Tasks (\n"
                + " id integer PRIMARY KEY,\n"
                + " title text NOT NULL,\n"
                + " description text,\n"
                + " due_date text,\n"
                + " completion_status boolean DEFAULT false\n"
                + ");\n";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    private static void insertTaskIntoDatabase(ToDo task) throws SQLException {
        String sql = "INSERT INTO Tasks(title, description, due_date, completion_status) VALUES(?,?,?,?)";
        try (var pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getDueDate());
            pstmt.setBoolean(4, task.isCompleted());
            pstmt.executeUpdate();
            try (var rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    task.setId(rs.getInt(1));
                }
            }
        }
    }

    private static void updateTaskStatusInDatabase(ToDo task) throws SQLException {
        String sql = "UPDATE Tasks SET completion_status = ? WHERE id = ?";
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, task.isCompleted());
            pstmt.setInt(2, task.getId());
            pstmt.executeUpdate();
        }
    }

    private static void deleteTaskFromDatabase(int id) throws SQLException {
        String sql = "DELETE FROM Tasks WHERE id = ?";
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting task from database: " + e.getMessage());
        }
    }

    private static void deleteTaskFromDatabase(String title) throws SQLException {
        String sql = "DELETE FROM Tasks WHERE title = ?";
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting task from database: " + e.getMessage());
        }
    }

    private static ArrayList<ToDo> getData() {
        ArrayList<ToDo> tasks = new ArrayList<>();
        String sql = "SELECT * FROM Tasks";
        try (var stmt = conn.createStatement();
                var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tasks.add(new ToDo(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("due_date"),
                        rs.getBoolean("completion_status")));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving tasks from database: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Close the shared database connection if it's open.
     */
    private static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }
}