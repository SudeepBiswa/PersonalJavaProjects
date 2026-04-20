import java.time.LocalDate;
import java.util.List;

public class CalendarService {
    private TaskCalendar taskCalendar;

    public CalendarService() {
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
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public CalendarTaskEntry scheduleTask(CalendarTaskEntry entry) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void rescheduleTask(int entryId, LocalDate newDate) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void unscheduleTask(int entryId) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<CalendarTaskEntry> getTasksForDate(LocalDate date) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<CalendarTaskEntry> getUpcomingTasks(LocalDate startDate, int numberOfDays) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean hasSchedulingConflict(CalendarTaskEntry entry) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
