import java.time.LocalDate;
import java.time.LocalDateTime;

public class CalendarTaskEntry {
    private int id;
    private ToDo task;
    private LocalDate scheduledDate;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private boolean allDay;
    private String notes;
    private String colorCode;

    public CalendarTaskEntry() {
    }

    public CalendarTaskEntry(
            int id,
            ToDo task,
            LocalDate scheduledDate,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            boolean allDay,
            String notes,
            String colorCode) {
        this.id = id;
        this.task = task;
        this.scheduledDate = scheduledDate;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.allDay = allDay;
        this.notes = notes;
        this.colorCode = colorCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ToDo getTask() {
        return task;
    }

    public void setTask(ToDo task) {
        this.task = task;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public boolean overlaps(CalendarTaskEntry otherEntry) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean occursOn(LocalDate date) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean hasTimeRange() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
