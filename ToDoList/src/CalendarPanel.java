import java.awt.BorderLayout;
import java.time.LocalDate;
import javax.swing.JPanel;

public class CalendarPanel extends JPanel {
    private CalendarService calendarService;
    private CalendarViewMode currentViewMode;
    private LocalDate focusedDate;

    public CalendarPanel() {
        setLayout(new BorderLayout());
    }

    public CalendarPanel(CalendarService calendarService) {
        this();
        this.calendarService = calendarService;
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
    }

    public LocalDate getFocusedDate() {
        return focusedDate;
    }

    public void setFocusedDate(LocalDate focusedDate) {
        this.focusedDate = focusedDate;
    }

    public void initializeComponents() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void refreshCalendar() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void showDayView() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void showWeekView() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void showMonthView() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void selectDate(LocalDate date) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void openTaskEntry(CalendarTaskEntry entry) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
