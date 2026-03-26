package seedu.address.model.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class ApplicationEventTest {

    private static final LocalDateTime VALID_DATETIME = LocalDateTime.of(2026, 12, 31, 23, 59);

    // Concrete subclass for testing the abstract class
    private static class ConcreteApplicationEvent extends ApplicationEvent {
        public ConcreteApplicationEvent(String location, LocalDateTime dateTime) {
            super(location, dateTime);
        }
    }

    @Test
    public void getLocation_validLocation_returnsLocation() {
        ApplicationEvent event = new ConcreteApplicationEvent("Singapore", VALID_DATETIME);
        assertEquals("Singapore", event.getLocation());
    }

    @Test
    public void getLocation_emptyLocation_returnsEmptyString() {
        ApplicationEvent event = new ConcreteApplicationEvent("", VALID_DATETIME);
        assertEquals("", event.getLocation());
    }

    @Test
    public void getLocation_locationWithSpaces_returnsLocation() {
        ApplicationEvent event = new ConcreteApplicationEvent("123 Main Street", VALID_DATETIME);
        assertEquals("123 Main Street", event.getLocation());
    }

    @Test
    public void getLocalDate_validDateTime_returnsDateTime() {
        ApplicationEvent event = new ConcreteApplicationEvent("Singapore", VALID_DATETIME);
        assertEquals(VALID_DATETIME, event.getLocalDate());
    }

    @Test
    public void isValidDateTime_validFormat_returnsTrue() {
        assertTrue(ApplicationEvent.isValidDateTime("2026-12-31 23:59"));
        assertTrue(ApplicationEvent.isValidDateTime("2026-01-01 00:00"));
    }

    @Test
    public void isValidDateTime_invalidFormat_returnsFalse() {
        assertFalse(ApplicationEvent.isValidDateTime("31-12-2026 23:59")); // wrong order
        assertFalse(ApplicationEvent.isValidDateTime("2026/12/31 23:59")); // wrong separator
        assertFalse(ApplicationEvent.isValidDateTime("2026-12-31T23:59")); // T instead of space
        assertFalse(ApplicationEvent.isValidDateTime("")); // blank
        assertFalse(ApplicationEvent.isValidDateTime("   ")); // whitespace only
    }
}
