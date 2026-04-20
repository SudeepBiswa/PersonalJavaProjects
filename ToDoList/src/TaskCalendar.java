import java.time.LocalDate;
import java.util.List;

public class TaskCalendar {
    private int id;
    private String name;
    private String description;
    private CalendarViewMode defaultViewMode;
    private LocalDate selectedDate;
    private List<CalendarTaskEntry> entries;

    public TaskCalendar() {
    }

    public TaskCalendar(
            int id,
            String name,
            String description,
            CalendarViewMode defaultViewMode,
            LocalDate selectedDate,
            List<CalendarTaskEntry> entries) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.defaultViewMode = defaultViewMode;
        this.selectedDate = selectedDate;
        this.entries = entries;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CalendarViewMode getDefaultViewMode() {
        return defaultViewMode;
    }

    public void setDefaultViewMode(CalendarViewMode defaultViewMode) {
        this.defaultViewMode = defaultViewMode;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    public List<CalendarTaskEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<CalendarTaskEntry> entries) {
        this.entries = entries;
    }

    public void addEntry(CalendarTaskEntry entry) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void removeEntry(CalendarTaskEntry entry) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<CalendarTaskEntry> getEntriesForDate(LocalDate date) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<CalendarTaskEntry> getEntriesForRange(LocalDate startDate, LocalDate endDate) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void changeViewMode(CalendarViewMode viewMode) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
