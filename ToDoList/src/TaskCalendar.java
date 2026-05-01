import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskCalendar {
    private int id;
    private String name;
    private String description;
    private CalendarViewMode defaultViewMode;
    private LocalDate selectedDate;
    private List<CalendarTaskEntry> entries;

    // Creates an empty month calendar for today.
    public TaskCalendar() {
        this.defaultViewMode = CalendarViewMode.MONTH;
        this.selectedDate = LocalDate.now();
        this.entries = new ArrayList<>();
    }

    // Creates a calendar with the given starting data.
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
        this.entries = entries == null ? new ArrayList<>() : entries;
    }

    // Gives back the calendar id.
    public int getId() {
        return id;
    }

    // Sets the calendar id.
    public void setId(int id) {
        this.id = id;
    }

    // Gives back the calendar name.
    public String getName() {
        return name;
    }

    // Sets the calendar name.
    public void setName(String name) {
        this.name = name;
    }

    // Gives back the calendar description.
    public String getDescription() {
        return description;
    }

    // Sets the calendar description.
    public void setDescription(String description) {
        this.description = description;
    }

    // Gives back the default view type.
    public CalendarViewMode getDefaultViewMode() {
        return defaultViewMode;
    }

    // Sets the default view type.
    public void setDefaultViewMode(CalendarViewMode defaultViewMode) {
        this.defaultViewMode = defaultViewMode;
    }

    // Gives back the selected date.
    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    // Sets the selected date.
    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    // Gives back all calendar entries.
    public List<CalendarTaskEntry> getEntries() {
        return entries;
    }

    // Sets all calendar entries.
    public void setEntries(List<CalendarTaskEntry> entries) {
        this.entries = entries == null ? new ArrayList<>() : entries;
    }

    // Adds one entry to the calendar.
    public void addEntry(CalendarTaskEntry entry) {
        if (entry == null) {
            return;
        }
        if (entries == null) {
            entries = new ArrayList<>();
        }
        entries.add(entry);
    }

    // Removes one entry from the calendar.
    public void removeEntry(CalendarTaskEntry entry) {
        if (entries != null) {
            entries.remove(entry);
        }
    }

    // Gets entries that happen on one date.
    public List<CalendarTaskEntry> getEntriesForDate(LocalDate date) {
        List<CalendarTaskEntry> results = new ArrayList<>();
        if (date == null || entries == null) {
            return results;
        }
        for (CalendarTaskEntry entry : entries) {
            if (entry != null && entry.occursOn(date)) {
                results.add(entry);
            }
        }
        return results;
    }

    // Gets entries between two dates.
    public List<CalendarTaskEntry> getEntriesForRange(LocalDate startDate, LocalDate endDate) {
        List<CalendarTaskEntry> results = new ArrayList<>();
        if (startDate == null || endDate == null || entries == null || endDate.isBefore(startDate)) {
            return results;
        }
        for (CalendarTaskEntry entry : entries) {
            if (entry == null || entry.getScheduledDate() == null) {
                continue;
            }
            LocalDate scheduledDate = entry.getScheduledDate();
            if (!scheduledDate.isBefore(startDate) && !scheduledDate.isAfter(endDate)) {
                results.add(entry);
            }
        }
        return results;
    }

    // Changes the saved default view type.
    public void changeViewMode(CalendarViewMode viewMode) {
        if (viewMode != null) {
            defaultViewMode = viewMode;
        }
    }
}
