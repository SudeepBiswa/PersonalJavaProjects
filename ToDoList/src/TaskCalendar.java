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

    public TaskCalendar() {
        this.defaultViewMode = CalendarViewMode.MONTH;
        this.selectedDate = LocalDate.now();
        this.entries = new ArrayList<>();
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
        this.entries = entries == null ? new ArrayList<>() : entries;
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
        this.entries = entries == null ? new ArrayList<>() : entries;
    }

    public void addEntry(CalendarTaskEntry entry) {
        if (entry == null) {
            return;
        }
        if (entries == null) {
            entries = new ArrayList<>();
        }
        entries.add(entry);
    }

    public void removeEntry(CalendarTaskEntry entry) {
        if (entries != null) {
            entries.remove(entry);
        }
    }

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

    public void changeViewMode(CalendarViewMode viewMode) {
        if (viewMode != null) {
            defaultViewMode = viewMode;
        }
    }
}
