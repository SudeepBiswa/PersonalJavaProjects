import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CalendarSystemTest {
    public static void main(String[] args) {
        TaskCalendar calendar = new TaskCalendar(
                1,
                "Test Calendar",
                "Calendar behavior test",
                CalendarViewMode.MONTH,
                LocalDate.of(2026, 4, 30),
                new ArrayList<>());
        CalendarService service = new CalendarService(calendar);

        ToDo study = new ToDo(10, "Study", "Review calendar code", "04/30/2026", false);
        CalendarTaskEntry studyEntry = service.scheduleTask(study, LocalDate.of(2026, 4, 30));

        assertEquals(1, service.getTasksForDate(LocalDate.of(2026, 4, 30)).size(), "scheduled task should appear on its date");
        assertEquals(0, service.getTasksForDate(LocalDate.of(2026, 5, 1)).size(), "scheduled task should not appear on another date");

        service.rescheduleTask(studyEntry.getId(), LocalDate.of(2026, 5, 1));
        assertEquals(0, service.getTasksForDate(LocalDate.of(2026, 4, 30)).size(), "rescheduled task should leave old date");
        assertEquals(1, service.getTasksForDate(LocalDate.of(2026, 5, 1)).size(), "rescheduled task should appear on new date");

        CalendarTaskEntry firstMeeting = new CalendarTaskEntry(
                20,
                new ToDo(20, "Meeting", "First meeting", "05/02/2026", false),
                LocalDate.of(2026, 5, 2),
                LocalDateTime.of(2026, 5, 2, 9, 0),
                LocalDateTime.of(2026, 5, 2, 10, 0),
                false,
                "",
                null);
        CalendarTaskEntry secondMeeting = new CalendarTaskEntry(
                21,
                new ToDo(21, "Overlap", "Second meeting", "05/02/2026", false),
                LocalDate.of(2026, 5, 2),
                LocalDateTime.of(2026, 5, 2, 9, 30),
                LocalDateTime.of(2026, 5, 2, 10, 30),
                false,
                "",
                null);

        service.scheduleTask(firstMeeting);
        assertTrue(service.hasSchedulingConflict(secondMeeting), "overlapping timed entries should conflict");

        List<CalendarTaskEntry> upcoming = service.getUpcomingTasks(LocalDate.of(2026, 5, 1), 2);
        assertEquals(2, upcoming.size(), "upcoming range should include both May 1 and May 2 entries");

        service.unscheduleTask(studyEntry.getId());
        assertEquals(0, service.getTasksForDate(LocalDate.of(2026, 5, 1)).size(), "unscheduled task should be removed");

        System.out.println("CalendarSystemTest passed");
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message + " Expected " + expected + " but got " + actual + ".");
        }
    }

    private static void assertTrue(boolean value, String message) {
        if (!value) {
            throw new AssertionError(message);
        }
    }
}
