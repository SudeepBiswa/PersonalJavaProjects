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

    // Creates an empty calendar entry.
    public CalendarTaskEntry() {
    }

    // Creates a calendar entry with all details set.
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

    // Gives back the entry id.
    public int getId() {
        return id;
    }

    // Sets the entry id.
    public void setId(int id) {
        this.id = id;
    }

    // Gives back the task for this entry.
    public ToDo getTask() {
        return task;
    }

    // Sets the task for this entry.
    public void setTask(ToDo task) {
        this.task = task;
    }

    // Gives back the date for this entry.
    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    // Sets the date for this entry.
    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    // Gives back the start date and time.
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    // Sets the start date and time.
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    // Gives back the end date and time.
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    // Sets the end date and time.
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    // Says if this entry lasts all day.
    public boolean isAllDay() {
        return allDay;
    }

    // Sets if this entry lasts all day.
    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    // Gives back the notes.
    public String getNotes() {
        return notes;
    }

    // Sets the notes.
    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Gives back the color code.
    public String getColorCode() {
        return colorCode;
    }

    // Sets the color code.
    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    // Checks if this entry overlaps another entry.
    public boolean overlaps(CalendarTaskEntry otherEntry) {
        if (otherEntry == null || !occursOn(otherEntry.getScheduledDate())) {
            return false;
        }
        if (isAllDay() || otherEntry.isAllDay()) {
            return true;
        }
        if (!hasTimeRange() || !otherEntry.hasTimeRange()) {
            return false;
        }
        return startDateTime.isBefore(otherEntry.getEndDateTime())
                && endDateTime.isAfter(otherEntry.getStartDateTime());
    }

    // Checks if this entry happens on the given date.
    public boolean occursOn(LocalDate date) {
        return date != null && date.equals(scheduledDate);
    }

    // Checks if this entry has a valid start and end time.
    public boolean hasTimeRange() {
        return startDateTime != null
                && endDateTime != null
                && startDateTime.isBefore(endDateTime);
    }
}
