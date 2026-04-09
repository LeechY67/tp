package seedu.address.model.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class DeadlineTest {

    @Test
    public void constructor_null_treatedAsEmptyDeadline() {
        assertTrue(new Deadline(null).isEmpty());
    }

    @Test
    public void compareTo() {
        Deadline early = new Deadline("2026-01-01");
        Deadline late = new Deadline("2026-12-31");
        Deadline empty = Deadline.getEmptyDeadline();

        // early < late
        assertTrue(early.compareTo(late) < 0);
        // late > early
        assertTrue(late.compareTo(early) > 0);
        // empty 排在最后 (假设你在 compareTo 里写了空值检查)
        assertTrue(late.compareTo(empty) < 0);
    }

    @Test
    public void compareTo_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Deadline("2026-01-01").compareTo(null));
    }

    @Test
    public void equals() {
        Deadline deadline = new Deadline("2026-12-31");

        // same values -> returns true
        assertTrue(deadline.equals(new Deadline("2026-12-31")));

        // same object -> returns true
        assertTrue(deadline.equals(deadline));

        // null -> returns false
        assertFalse(deadline.equals(null));

        // different types -> returns false
        assertFalse(deadline.equals(5.0f));

        // different values -> returns false
        assertFalse(deadline.equals(new Deadline("2026-01-01")));
    }

    @Test
    public void isValidFormat() {
        // null and empty inputs
        assertThrows(NullPointerException.class, () -> Deadline.isValidFormat(null));
        assertFalse(Deadline.isValidFormat("")); // empty string
        assertFalse(Deadline.isValidFormat(" ")); // spaces only

        // invalid formats
        assertFalse(Deadline.isValidFormat("2026/01/01")); // wrong separators
        assertFalse(Deadline.isValidFormat("01-01-2026")); // wrong order
        assertFalse(Deadline.isValidFormat("2026-1-1")); // missing leading zeros
        assertFalse(Deadline.isValidFormat("tomorrow")); // completely wrong format

        // valid formats (even if the date doesn't exist on calendar)
        assertTrue(Deadline.isValidFormat("2026-01-90")); // format is correct, but date is fake
        assertTrue(Deadline.isValidFormat("2023-02-29")); // format is correct, but not a leap year

        // valid formats with correct dates
        assertTrue(Deadline.isValidFormat("2026-12-31")); // Date format
        assertTrue(Deadline.isValidFormat("2026-12-31 23:59")); // DateTime format

        // placeholder
        assertTrue(Deadline.isValidFormat("-"));
    }

    @Test
    public void isValidCalendarDate() {
        assertThrows(NullPointerException.class, () -> Deadline.isValidCalendarDate(null));

        // format is correct, but date does NOT exist on the calendar -> should return false
        assertFalse(Deadline.isValidCalendarDate("2026-01-90")); // 90th day of Jan does not exist
        assertFalse(Deadline.isValidCalendarDate("2026-13-01")); // 13th month does not exist
        assertFalse(Deadline.isValidCalendarDate("2023-02-29")); // 2023 is not a leap year
        assertFalse(Deadline.isValidCalendarDate("2026-12-31 25:00")); // 25th hour does not exist
        assertFalse(Deadline.isValidCalendarDate("2026-12-31 23:60")); // 60th minute does not exist

        // valid dates that DO exist on the calendar -> should return true
        assertTrue(Deadline.isValidCalendarDate("2026-01-31"));
        assertTrue(Deadline.isValidCalendarDate("2024-02-29")); // 2024 is a leap year!
        assertTrue(Deadline.isValidCalendarDate("2026-12-31 23:59")); // valid DateTime

        // placeholder
        assertTrue(Deadline.isValidCalendarDate("-"));
    }

    @Test
    public void isValidDeadline() { // make sure former code works
        // both format and calendar date must be correct
        assertFalse(Deadline.isValidDeadline("2026/01/01")); // fails format
        assertFalse(Deadline.isValidDeadline("2026-01-90")); // passes format, fails calendar

        assertTrue(Deadline.isValidDeadline("2026-01-01")); // passes both
        assertTrue(Deadline.isValidDeadline("-")); // passes both
    }

    @Test
    public void getLocalDate_validFormats_returnsParsedDate() {
        assertEquals(LocalDate.of(2026, 12, 31), new Deadline("2026-12-31").getLocalDate());
        assertEquals(LocalDate.of(2026, 3, 23), new Deadline("2026-03-23 15:00").getLocalDate());
    }

    @Test
    public void getLocalDate_emptyOrPlaceholder_returnsNull() {
        assertNull(Deadline.getEmptyDeadline().getLocalDate());
        assertNull(new Deadline("-").getLocalDate());
    }

    @Test
    public void getLocalDate_invalidDateString_returnsNull() {
        // Length >= 10 but not parseable as LocalDate -> falls into catch block.
        assertNull(new Deadline("2026-99-99").getLocalDate());
    }

    @Test
    public void isValidDeadline_placeholder_returnsTrue() {
        assertTrue(Deadline.isValidDeadline("-"));
    }

    @Test
    public void hashCode_sameValue_sameHash() {
        Deadline first = new Deadline("2026-12-31");
        Deadline second = new Deadline("2026-12-31");
        assertEquals(first.hashCode(), second.hashCode());
    }
}
