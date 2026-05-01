import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CalendarService {
    private TaskCalendar taskCalendar;

    public CalendarService() {
        this(new TaskCalendar());
    }

    public CalendarService(TaskCalendar taskCalendar) {
        this.taskCalendar = taskCalendar;
    }

    public TaskCalendar getTaskCalendar() {
        return taskCalendar;
    }

    public void setTaskCalendar(TaskCalendar taskCalendar) {
        this.taskCalendar = taskCalendar;
    }

    public CalendarTaskEntry scheduleTask(ToDo task, LocalDate date) {
        if (task == null || date == null) {
            return null;
        }
        CalendarTaskEntry entry = new CalendarTaskEntry(
                task.getId(),
                task,
                date,
                null,
                null,
                true,
                task.getDescription(),
                null);
        return scheduleTask(entry);
    }

    public CalendarTaskEntry scheduleTask(CalendarTaskEntry entry) {
        if (entry == null || entry.getScheduledDate() == null) {
            return null;
        }
        ensureCalendar();
        taskCalendar.addEntry(entry);
        return entry;
    }

    public void rescheduleTask(int entryId, LocalDate newDate) {
        CalendarTaskEntry entry = findEntryById(entryId);
        if (entry != null && newDate != null) {
            entry.setScheduledDate(newDate);
            if (entry.getStartDateTime() != null) {
                entry.setStartDateTime(newDate.atTime(entry.getStartDateTime().toLocalTime()));
            }
            if (entry.getEndDateTime() != null) {
                entry.setEndDateTime(newDate.atTime(entry.getEndDateTime().toLocalTime()));
            }
        }
    }

    public void unscheduleTask(int entryId) {
        ensureCalendar();
        CalendarTaskEntry entry = findEntryById(entryId);
        if (entry != null) {
            taskCalendar.removeEntry(entry);
        }
    }

    public List<CalendarTaskEntry> getTasksForDate(LocalDate date) {
        ensureCalendar();
        return taskCalendar.getEntriesForDate(date);
    }

    public List<CalendarTaskEntry> getUpcomingTasks(LocalDate startDate, int numberOfDays) {
        ensureCalendar();
        if (startDate == null || numberOfDays <= 0) {
            return new ArrayList<>();
        }
        LocalDate endDate = startDate.plusDays(numberOfDays - 1);
        List<CalendarTaskEntry> upcomingEntries = taskCalendar.getEntriesForRange(startDate, endDate);
        upcomingEntries.sort(Comparator
                .comparing(CalendarTaskEntry::getScheduledDate)
                .thenComparing(entry -> entry.getStartDateTime() == null
                        ? entry.getScheduledDate().atStartOfDay()
                        : entry.getStartDateTime()));
        return upcomingEntries;
    }

    public boolean hasSchedulingConflict(CalendarTaskEntry entry) {
        ensureCalendar();
        if (entry == null) {
            return false;
        }
        for (CalendarTaskEntry existingEntry : taskCalendar.getEntries()) {
            if (existingEntry == null || existingEntry == entry || existingEntry.getId() == entry.getId()) {
                continue;
            }
            if (entry.overlaps(existingEntry)) {
                return true;
            }
        }
        return false;
    }

    private void ensureCalendar() {
        if (taskCalendar == null) {
            taskCalendar = new TaskCalendar();
        }
    }

    private CalendarTaskEntry findEntryById(int entryId) {
        ensureCalendar();
        for (CalendarTaskEntry entry : taskCalendar.getEntries()) {
            if (entry != null && entry.getId() == entryId) {
                return entry;
            }
        }
        return null;
    }
}
