import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class CalendarPanel extends JPanel {
    private static final Color PAGE_BACKGROUND = new Color(239, 243, 248);
    private static final Color CARD_BACKGROUND = new Color(250, 252, 255);
    private static final Color SOFT_BACKGROUND = new Color(232, 239, 248);
    private static final Color PRIMARY = new Color(76, 119, 179);
    private static final Color PRIMARY_SOFT = new Color(214, 228, 247);
    private static final Color TEXT = new Color(44, 57, 77);
    private static final Color MUTED = new Color(114, 128, 150);
    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("MMMM yyyy");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("EEEE, MMM d");
    private static final String MONTH_VIEW_CARD = "month";
    private static final String WEEK_VIEW_CARD = "week";
    private static final String DAY_VIEW_CARD = "day";

    private CalendarService calendarService;
    private CalendarViewMode currentViewMode;
    private LocalDate focusedDate;

    private JLabel monthTitleLabel;
    private JLabel selectedDateLabel;
    private JPanel monthGridPanel;
    private JPanel weekGridPanel;
    private JPanel calendarContentPanel;
    private JPanel dayTasksPanel;
    private CardLayout contentLayout;
    private JButton dayButton;
    private JButton weekButton;
    private JButton monthButton;

    public CalendarPanel() {
        this(null);
    }

    public CalendarPanel(CalendarService calendarService) {
        this.calendarService = calendarService;
        this.currentViewMode = CalendarViewMode.MONTH;
        this.focusedDate = LocalDate.now();
        initializeComponents();
        refreshCalendar();
    }

    public CalendarService getCalendarService() {
        return calendarService;
    }

    public void setCalendarService(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    public CalendarViewMode getCurrentViewMode() {
        return currentViewMode;
    }

    public void setCurrentViewMode(CalendarViewMode currentViewMode) {
        this.currentViewMode = currentViewMode;
        updateViewButtons();
    }

    public LocalDate getFocusedDate() {
        return focusedDate;
    }

    public void setFocusedDate(LocalDate focusedDate) {
        this.focusedDate = focusedDate;
        refreshCalendar();
    }

    public void initializeComponents() {
        removeAll();
        setLayout(new BorderLayout(16, 16));
        setBackground(PAGE_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
    }

    public void refreshCalendar() {
        monthTitleLabel.setText(focusedDate.format(MONTH_FORMAT));
        rebuildMonthGrid();
        rebuildDayTaskList();
        updateVisibleView();
        revalidate();
        repaint();
    }

    public void showDayView() {
        setCurrentViewMode(CalendarViewMode.DAY);
        refreshCalendar();
    }

    public void showWeekView() {
        setCurrentViewMode(CalendarViewMode.WEEK);
        refreshCalendar();
    }

    public void showMonthView() {
        setCurrentViewMode(CalendarViewMode.MONTH);
        refreshCalendar();
    }

    public void selectDate(LocalDate date) {
        this.focusedDate = date;
        refreshCalendar();
    }

    public void openTaskEntry(CalendarTaskEntry entry) {
        JOptionPane.showMessageDialog(
                this,
                "This is just the calendar UI for now.\nTask scheduling can be connected later.",
                "Calendar Entry",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel buildTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(12, 0));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 8, 4));

        JLabel titleLabel = new JLabel("Calendar");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(TEXT);

        monthTitleLabel = new JLabel();
        monthTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        monthTitleLabel.setForeground(PRIMARY);

        JButton previousButton = createFlatButton("<");
        JButton todayButton = createFlatButton("Today");
        JButton nextButton = createFlatButton(">");
        dayButton = createFlatButton("Day");
        weekButton = createFlatButton("Week");
        monthButton = createFlatButton("Month");

        previousButton.setPreferredSize(new Dimension(48, 28));
        todayButton.setPreferredSize(new Dimension(82, 28));
        nextButton.setPreferredSize(new Dimension(48, 28));
        dayButton.setPreferredSize(new Dimension(56, 28));
        weekButton.setPreferredSize(new Dimension(66, 28));
        monthButton.setPreferredSize(new Dimension(72, 28));

        previousButton.addActionListener(event -> {
            focusedDate = focusedDate.minusMonths(1);
            refreshCalendar();
        });
        todayButton.addActionListener(event -> {
            focusedDate = LocalDate.now();
            refreshCalendar();
        });
        nextButton.addActionListener(event -> {
            focusedDate = focusedDate.plusMonths(1);
            refreshCalendar();
        });
        dayButton.addActionListener(event -> showDayView());
        weekButton.addActionListener(event -> showWeekView());
        monthButton.addActionListener(event -> showMonthView());

        JPanel navigationRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        navigationRow.setOpaque(false);
        navigationRow.add(previousButton);
        navigationRow.add(todayButton);
        navigationRow.add(nextButton);

        JPanel viewRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        viewRow.setOpaque(false);
        viewRow.add(dayButton);
        viewRow.add(weekButton);
        viewRow.add(monthButton);

        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add(navigationRow);
        controlPanel.add(Box.createVerticalStrut(8));
        controlPanel.add(viewRow);

        JPanel rightSpacer = new JPanel();
        rightSpacer.setOpaque(false);
        rightSpacer.setPreferredSize(controlPanel.getPreferredSize());

        JPanel middlePanel = new JPanel();
        middlePanel.setOpaque(false);
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        monthTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        monthTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        middlePanel.add(titleLabel);
        middlePanel.add(Box.createVerticalStrut(4));
        middlePanel.add(monthTitleLabel);

        topPanel.add(controlPanel, BorderLayout.WEST);
        topPanel.add(middlePanel, BorderLayout.CENTER);
        topPanel.add(rightSpacer, BorderLayout.EAST);
        return topPanel;
    }

    private JPanel buildCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        contentLayout = new CardLayout();
        calendarContentPanel = new JPanel(contentLayout);
        calendarContentPanel.setOpaque(false);

        JPanel calendarCard = createGridPanel(new BorderLayout(0, 12));
        JPanel weekdaysPanel = new JPanel(new GridLayout(1, 7, 8, 8));
        weekdaysPanel.setOpaque(false);

        DayOfWeek[] days = {
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY
        };

        for (DayOfWeek day : days) {
            JLabel label = new JLabel(day.getDisplayName(TextStyle.SHORT, Locale.US), SwingConstants.CENTER);
            label.setForeground(MUTED);
            label.setFont(new Font("SansSerif", Font.BOLD, 12));
            weekdaysPanel.add(label);
        }

        monthGridPanel = new JPanel(new GridLayout(6, 7, 8, 8));
        monthGridPanel.setOpaque(false);

        calendarCard.add(weekdaysPanel, BorderLayout.NORTH);
        calendarCard.add(monthGridPanel, BorderLayout.CENTER);

        JPanel weekViewCard = createGridPanel(new BorderLayout(0, 12));
        JPanel weekHeaderPanel = new JPanel(new GridLayout(1, 7, 8, 8));
        weekHeaderPanel.setOpaque(false);

        for (DayOfWeek day : days) {
            JLabel label = new JLabel(day.getDisplayName(TextStyle.SHORT, Locale.US), SwingConstants.CENTER);
            label.setForeground(MUTED);
            label.setFont(new Font("SansSerif", Font.BOLD, 12));
            weekHeaderPanel.add(label);
        }

        weekGridPanel = new JPanel(new GridLayout(1, 7, 8, 8));
        weekGridPanel.setOpaque(false);

        weekViewCard.add(weekHeaderPanel, BorderLayout.NORTH);
        weekViewCard.add(weekGridPanel, BorderLayout.CENTER);

        JPanel dayViewPanel = createGridPanel(new BorderLayout(0, 12));

        JLabel infoTitle = new JLabel("Tasks For Day");
        infoTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        infoTitle.setForeground(TEXT);

        selectedDateLabel = new JLabel();
        selectedDateLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        selectedDateLabel.setForeground(MUTED);

        JPanel dayHeaderPanel = new JPanel();
        dayHeaderPanel.setOpaque(false);
        dayHeaderPanel.setLayout(new BoxLayout(dayHeaderPanel, BoxLayout.Y_AXIS));
        dayHeaderPanel.add(infoTitle);
        dayHeaderPanel.add(Box.createVerticalStrut(10));
        dayHeaderPanel.add(selectedDateLabel);

        dayTasksPanel = new JPanel();
        dayTasksPanel.setOpaque(false);
        dayTasksPanel.setLayout(new BoxLayout(dayTasksPanel, BoxLayout.Y_AXIS));

        JScrollPane tasksScrollPane = new JScrollPane(dayTasksPanel);
        tasksScrollPane.setBorder(BorderFactory.createEmptyBorder());
        tasksScrollPane.getViewport().setBackground(PAGE_BACKGROUND);
        tasksScrollPane.setOpaque(false);

        dayViewPanel.add(dayHeaderPanel, BorderLayout.NORTH);
        dayViewPanel.add(tasksScrollPane, BorderLayout.CENTER);

        calendarContentPanel.add(calendarCard, MONTH_VIEW_CARD);
        calendarContentPanel.add(weekViewCard, WEEK_VIEW_CARD);
        calendarContentPanel.add(dayViewPanel, DAY_VIEW_CARD);
        centerPanel.add(calendarContentPanel, BorderLayout.CENTER);
        return centerPanel;
    }

    private void rebuildDayTaskList() {
        if (dayTasksPanel == null) {
            return;
        }

        dayTasksPanel.removeAll();
        selectedDateLabel.setText(focusedDate.format(DATE_FORMAT));
        List<CalendarTaskEntry> dayEntries = getEntriesForFocusedDate();

        if (dayEntries.isEmpty()) {
            JTextArea emptyArea = new JTextArea(
                    "No tasks are scheduled for this day yet.\n\n"
                            + "When calendar entries are added, they will show up here.");
            emptyArea.setEditable(false);
            emptyArea.setLineWrap(true);
            emptyArea.setWrapStyleWord(true);
            emptyArea.setBackground(SOFT_BACKGROUND);
            emptyArea.setForeground(TEXT);
            emptyArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
            emptyArea.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            emptyArea.setAlignmentX(Component.LEFT_ALIGNMENT);
            dayTasksPanel.add(emptyArea);
            return;
        }

        for (CalendarTaskEntry entry : dayEntries) {
            dayTasksPanel.add(createTaskListCard(entry));
            dayTasksPanel.add(Box.createVerticalStrut(10));
        }
    }

    private JPanel createTaskListCard(CalendarTaskEntry entry) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBackground(SOFT_BACKGROUND);
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        String title = entry.getTask() == null ? "Untitled Task" : entry.getTask().getTitle();
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setForeground(TEXT);

        String timeText = entry.isAllDay() ? "All day" : buildTimeText(entry);
        JLabel timeLabel = new JLabel(timeText);
        timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        timeLabel.setForeground(MUTED);

        String notes = entry.getNotes() == null || entry.getNotes().isBlank()
                ? "No extra notes."
                : entry.getNotes();
        JTextArea notesArea = new JTextArea(notes);
        notesArea.setEditable(false);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setBackground(SOFT_BACKGROUND);
        notesArea.setForeground(TEXT);
        notesArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        notesArea.setBorder(BorderFactory.createEmptyBorder());

        JButton openButton = createFlatButton("Open");
        openButton.addActionListener(event -> openTaskEntry(entry));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.add(titleLabel, BorderLayout.WEST);
        topRow.add(openButton, BorderLayout.EAST);

        card.add(topRow, BorderLayout.NORTH);
        card.add(timeLabel, BorderLayout.CENTER);
        card.add(notesArea, BorderLayout.SOUTH);
        return card;
    }

    private String buildTimeText(CalendarTaskEntry entry) {
        if (entry.getStartDateTime() != null && entry.getEndDateTime() != null) {
            return entry.getStartDateTime().toLocalTime() + " - " + entry.getEndDateTime().toLocalTime();
        }
        if (entry.getStartDateTime() != null) {
            return "Starts at " + entry.getStartDateTime().toLocalTime();
        }
        return "Time not set";
    }

    private List<CalendarTaskEntry> getEntriesForFocusedDate() {
        List<CalendarTaskEntry> entriesForDay = new ArrayList<>();
        if (calendarService == null || calendarService.getTaskCalendar() == null) {
            return entriesForDay;
        }

        List<CalendarTaskEntry> entries = calendarService.getTaskCalendar().getEntries();
        if (entries == null) {
            return entriesForDay;
        }

        for (CalendarTaskEntry entry : entries) {
            if (entry == null) {
                continue;
            }
            if (focusedDate.equals(entry.getScheduledDate())) {
                entriesForDay.add(entry);
            }
        }
        return entriesForDay;
    }

    private void rebuildMonthGrid() {
        monthGridPanel.removeAll();
        LocalDate firstDay = focusedDate.withDayOfMonth(1);
        int startOffset = firstDay.getDayOfWeek().getValue() % 7;
        LocalDate gridStart = firstDay.minusDays(startOffset);

        for (int index = 0; index < 42; index++) {
            LocalDate cellDate = gridStart.plusDays(index);
            monthGridPanel.add(createDayCell(cellDate));
        }

        updateViewButtons();
    }

    private void rebuildWeekGrid() {
        if (weekGridPanel == null) {
            return;
        }

        weekGridPanel.removeAll();
        LocalDate weekStart = focusedDate.minusDays(focusedDate.getDayOfWeek().getValue() % 7);

        for (int index = 0; index < 7; index++) {
            LocalDate cellDate = weekStart.plusDays(index);
            weekGridPanel.add(createWeekCell(cellDate));
        }
    }

    private JPanel createWeekCell(LocalDate cellDate) {
        JPanel cell = new JPanel(new BorderLayout(0, 8));
        cell.setOpaque(true);
        cell.setBackground(getDayColor(cellDate));
        cell.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(212, 221, 232)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JLabel dayNumberLabel = new JLabel(String.valueOf(cellDate.getDayOfMonth()));
        dayNumberLabel.setForeground(cellDate.equals(focusedDate) ? PRIMARY : TEXT);
        dayNumberLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel statusLabel = new JLabel(getCellText(cellDate));
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setForeground(MUTED);

        JButton openButton = createFlatButton("Open Day");
        openButton.addActionListener(event -> {
            focusedDate = cellDate;
            showDayView();
        });

        cell.add(dayNumberLabel, BorderLayout.NORTH);
        cell.add(statusLabel, BorderLayout.CENTER);
        cell.add(openButton, BorderLayout.SOUTH);
        return cell;
    }

    private JPanel createDayCell(LocalDate cellDate) {
        JPanel cell = new JPanel(new BorderLayout(0, 8));
        cell.setOpaque(true);
        cell.setBackground(getDayColor(cellDate));
        cell.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(212, 221, 232)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JButton dayButton = new JButton(String.valueOf(cellDate.getDayOfMonth()));
        dayButton.setBorderPainted(false);
        dayButton.setContentAreaFilled(false);
        dayButton.setFocusPainted(false);
        dayButton.setHorizontalAlignment(SwingConstants.LEFT);
        dayButton.setForeground(cellDate.equals(focusedDate) ? PRIMARY : TEXT);
        dayButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        dayButton.addActionListener(event -> {
            focusedDate = cellDate;
            showDayView();
        });

        JLabel taskLabel = new JLabel(getCellText(cellDate));
        taskLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        taskLabel.setForeground(MUTED);

        cell.add(dayButton, BorderLayout.NORTH);
        cell.add(taskLabel, BorderLayout.CENTER);
        return cell;
    }

    private Color getDayColor(LocalDate cellDate) {
        if (cellDate.equals(focusedDate)) {
            return PRIMARY_SOFT;
        }
        if (!cellDate.getMonth().equals(focusedDate.getMonth())) {
            return new Color(244, 247, 251);
        }
        return CARD_BACKGROUND;
    }

    private String getCellText(LocalDate cellDate) {
        if (cellDate.equals(LocalDate.now())) {
            return "Today";
        }
        if (cellDate.equals(focusedDate) && currentViewMode == CalendarViewMode.DAY) {
            return "Day view";
        }
        if (cellDate.equals(focusedDate)) {
            return "Selected";
        }
        if (currentViewMode == CalendarViewMode.WEEK) {
            return "Week view";
        }
        return "No tasks";
    }

    private void updateVisibleView() {
        if (contentLayout == null || calendarContentPanel == null) {
            return;
        }

        if (currentViewMode == CalendarViewMode.DAY) {
            contentLayout.show(calendarContentPanel, DAY_VIEW_CARD);
        } else if (currentViewMode == CalendarViewMode.WEEK) {
            rebuildWeekGrid();
            contentLayout.show(calendarContentPanel, WEEK_VIEW_CARD);
        } else {
            contentLayout.show(calendarContentPanel, MONTH_VIEW_CARD);
        }
    }

    private JPanel createGridPanel(BorderLayout layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(212, 221, 232)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        return panel;
    }

    private JButton createFlatButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(SOFT_BACKGROUND);
        button.setForeground(TEXT);
        button.setFont(new Font("SansSerif", Font.PLAIN, 12));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 217, 232)),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        return button;
    }

    private void updateViewButtons() {
        styleViewButton(dayButton, currentViewMode == CalendarViewMode.DAY);
        styleViewButton(weekButton, currentViewMode == CalendarViewMode.WEEK);
        styleViewButton(monthButton, currentViewMode == CalendarViewMode.MONTH);
    }

    private void styleViewButton(JButton button, boolean selected) {
        if (button == null) {
            return;
        }

        if (selected) {
            button.setBackground(PRIMARY);
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(SOFT_BACKGROUND);
            button.setForeground(TEXT);
        }
    }
}
