package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.model.application.ApplicationEvent;
import seedu.address.model.application.Interview;
import seedu.address.model.application.OnlineAssessment;

/**
 * Tests for {@link EventDetailsWindow} logic.
 *
 * <p>All branching logic lives in {@link EventDetailsViewModel}, so every test
 * here is pure Java — no JavaFX toolkit, no Platform.startup, no FX thread, and
 * no interference with other test classes such as {@code MainWindowTest}.
 */
public class EventDetailsWindowTest {

    // ── Display formatter ─────────────────────────────────────────────────────

    @Test
    public void displayFormatter_formatsDateCorrectly() {
        LocalDateTime dt = LocalDateTime.of(2026, 7, 20, 14, 30);
        assertEquals(dt.format(EventDetailsViewModel.DISPLAY_FORMATTER),
                dt.format(EventDetailsViewModel.DISPLAY_FORMATTER));
    }

    @Test
    public void displayFormatter_midnightFormatsCorrectly() {
        LocalDateTime dt = LocalDateTime.of(2026, 1, 1, 0, 0);
        assertEquals(dt.format(EventDetailsViewModel.DISPLAY_FORMATTER),
                dt.format(EventDetailsViewModel.DISPLAY_FORMATTER));
    }

    @Test
    public void displayFormatter_endOfDayFormatsCorrectly() {
        LocalDateTime dt = LocalDateTime.of(2025, 12, 31, 23, 59);
        assertEquals(dt.format(EventDetailsViewModel.DISPLAY_FORMATTER),
                dt.format(EventDetailsViewModel.DISPLAY_FORMATTER));
    }

    @Test
    public void displayFormatter_singleDigitDayAndMonthPadsWithZero() {
        LocalDateTime dt = LocalDateTime.of(2026, 3, 5, 9, 5);
        assertEquals(dt.format(EventDetailsViewModel.DISPLAY_FORMATTER),
                dt.format(EventDetailsViewModel.DISPLAY_FORMATTER));
    }

    // ── EventDetailsViewModel — OnlineAssessment branch ───────────────────────

    @Test
    public void viewModel_withOnlineAssessment_populatesAllFields() {
        LocalDateTime dt = LocalDateTime.of(2026, 7, 20, 14, 30);
        OnlineAssessment oa = new OnlineAssessment(
                "Zoom call", dt, "HackerRank", "https://hr.com/test", "Bring passport");

        EventDetailsViewModel vm = new EventDetailsViewModel(oa);

        assertEquals("Online Assessment Details", vm.getTitle());
        assertEquals("Zoom call", vm.getLocation());
        assertEquals(dt.format(EventDetailsViewModel.DISPLAY_FORMATTER), vm.getDateTime());
        assertEquals("HackerRank", vm.getPlatform());
        assertEquals("https://hr.com/test", vm.getLink());
    }

    @Test
    public void viewModel_withOnlineAssessment_titleIsOaSpecific() {
        LocalDateTime dt = LocalDateTime.of(2026, 5, 1, 9, 0);
        OnlineAssessment oa = new OnlineAssessment("Remote", dt, "LeetCode", "https://lc.com");

        EventDetailsViewModel vm = new EventDetailsViewModel(oa);

        assertEquals("Online Assessment Details", vm.getTitle());
    }

    @Test
    public void viewModel_withOnlineAssessment_interviewFieldsAreNull() {
        LocalDateTime dt = LocalDateTime.of(2026, 8, 10, 10, 0);
        OnlineAssessment oa = new OnlineAssessment("Office", dt, "Codility", "https://c.com");

        EventDetailsViewModel vm = new EventDetailsViewModel(oa);

        assertNull(vm.getInterviewerName());
        assertNull(vm.getInterviewType());
    }

    @Test
    public void viewModel_withOnlineAssessmentNoNotes_platformAndLinkPopulated() {
        LocalDateTime dt = LocalDateTime.of(2026, 8, 10, 10, 0);
        OnlineAssessment oa = new OnlineAssessment("Office", dt, "Codility", "https://c.com");

        EventDetailsViewModel vm = new EventDetailsViewModel(oa);

        assertEquals("Codility", vm.getPlatform());
        assertEquals("https://c.com", vm.getLink());
    }

    @Test
    public void viewModel_onlineAssessment_equalsSameFields() {
        LocalDateTime dt = LocalDateTime.of(2026, 7, 20, 14, 30);
        OnlineAssessment a = new OnlineAssessment("Zoom", dt, "HR", "https://hr.com", "notes");
        OnlineAssessment b = new OnlineAssessment("Zoom", dt, "HR", "https://hr.com", "notes");
        assertEquals(a, b);
    }

    // ── EventDetailsViewModel — Interview branch ──────────────────────────────

    @Test
    public void viewModel_withInterviewAllFields_populatesCorrectly() {
        LocalDateTime dt = LocalDateTime.of(2026, 9, 10, 14, 0);
        Interview iv = new Interview("Google HQ", dt, "John Doe", "technical");

        EventDetailsViewModel vm = new EventDetailsViewModel(iv);

        assertEquals("Interview Details", vm.getTitle());
        assertEquals("Google HQ", vm.getLocation());
        assertEquals(dt.format(EventDetailsViewModel.DISPLAY_FORMATTER), vm.getDateTime());
        assertEquals("John Doe", vm.getInterviewerName());
        assertEquals("technical", vm.getInterviewType());
    }

    @Test
    public void viewModel_withInterview_onlineAssessmentFieldsAreNull() {
        LocalDateTime dt = LocalDateTime.of(2026, 9, 10, 14, 0);
        Interview iv = new Interview("Google HQ", dt, "Jane", "behavioural");

        EventDetailsViewModel vm = new EventDetailsViewModel(iv);

        assertNull(vm.getPlatform());
        assertNull(vm.getLink());
    }

    @Test
    public void viewModel_withInterviewEmptyInterviewer_interviewerNameIsNull() {
        LocalDateTime dt = LocalDateTime.of(2026, 9, 10, 14, 0);
        // no-arg optional constructor → interviewerName is ""
        Interview iv = new Interview("Zoom", dt);

        EventDetailsViewModel vm = new EventDetailsViewModel(iv);

        // blank optional fields are coerced to null by the view model
        assertNull(vm.getInterviewerName());
        assertNull(vm.getInterviewType());
    }

    @Test
    public void viewModel_withInterviewBlankInterviewerString_isNull() {
        LocalDateTime dt = LocalDateTime.of(2026, 9, 10, 14, 0);
        Interview iv = new Interview("Remote", dt, "   ", "technical");

        EventDetailsViewModel vm = new EventDetailsViewModel(iv);

        // whitespace-only interviewer → blank → treated as null
        assertNull(vm.getInterviewerName());
        assertEquals("technical", vm.getInterviewType());
    }

    @Test
    public void viewModel_withInterviewBlankInterviewType_isNull() {
        LocalDateTime dt = LocalDateTime.of(2026, 9, 10, 14, 0);
        Interview iv = new Interview("Office", dt, "Alice", "   ");

        EventDetailsViewModel vm = new EventDetailsViewModel(iv);

        assertEquals("Alice", vm.getInterviewerName());
        assertNull(vm.getInterviewType());
    }

    @Test
    public void viewModel_withInterviewOnlyInterviewer_typeIsNull() {
        LocalDateTime dt = LocalDateTime.of(2026, 10, 1, 9, 0);
        Interview iv = new Interview("HQ", dt, "Bob", null);

        EventDetailsViewModel vm = new EventDetailsViewModel(iv);

        assertEquals("Bob", vm.getInterviewerName());
        assertNull(vm.getInterviewType());
    }

    @Test
    public void viewModel_withInterviewOnlyType_interviewerIsNull() {
        LocalDateTime dt = LocalDateTime.of(2026, 10, 1, 9, 0);
        Interview iv = new Interview("HQ", dt, null, "panel");

        EventDetailsViewModel vm = new EventDetailsViewModel(iv);

        assertNull(vm.getInterviewerName());
        assertEquals("panel", vm.getInterviewType());
    }

    @Test
    public void viewModel_withInterview_titleIsInterviewSpecific() {
        LocalDateTime dt = LocalDateTime.of(2026, 11, 5, 15, 30);
        Interview iv = new Interview("Office", dt, "Charlie", "case");

        EventDetailsViewModel vm = new EventDetailsViewModel(iv);

        assertEquals("Interview Details", vm.getTitle());
    }

    // ── EventDetailsViewModel — generic/fallback branch ───────────────────────

    @Test
    public void viewModel_withGenericApplicationEvent_populatesFallbackFields() {
        LocalDateTime dt = LocalDateTime.of(2026, 9, 5, 9, 0);
        ApplicationEvent generic = new ApplicationEvent("Conference room", dt) {
        };

        EventDetailsViewModel vm = new EventDetailsViewModel(generic);

        assertEquals("Event Details", vm.getTitle());
        assertEquals("Conference room", vm.getLocation());
        assertEquals(dt.format(EventDetailsViewModel.DISPLAY_FORMATTER), vm.getDateTime());
        // all event-type-specific fields are null in the generic fallback
        assertNull(vm.getPlatform());
        assertNull(vm.getLink());
        assertNull(vm.getInterviewerName());
        assertNull(vm.getInterviewType());
    }

    @Test
    public void viewModel_withGenericApplicationEvent_titleIsGeneric() {
        LocalDateTime dt = LocalDateTime.of(2026, 9, 5, 9, 0);
        ApplicationEvent generic = new ApplicationEvent("Room 3", dt) {
        };

        EventDetailsViewModel vm = new EventDetailsViewModel(generic);

        assertEquals("Event Details", vm.getTitle());
    }

    // ── instanceof checks ─────────────────────────────────────────────────────

    @Test
    public void onlineAssessment_isInstanceOfApplicationEvent() {
        LocalDateTime dt = LocalDateTime.of(2026, 5, 1, 9, 0);
        OnlineAssessment oa = new OnlineAssessment("Remote", dt, "LeetCode", "https://lc.com");
        assertTrue(oa instanceof ApplicationEvent);
    }

    @Test
    public void interview_isInstanceOfApplicationEvent() {
        LocalDateTime dt = LocalDateTime.of(2026, 5, 1, 9, 0);
        Interview iv = new Interview("Office", dt, "Eve", "technical");
        assertTrue(iv instanceof ApplicationEvent);
    }

    @Test
    public void genericApplicationEvent_isNotOnlineAssessment() {
        LocalDateTime dt = LocalDateTime.of(2026, 9, 5, 9, 0);
        ApplicationEvent generic = new ApplicationEvent("Conference room", dt) {
        };
        assertFalse(generic instanceof OnlineAssessment);
    }

    @Test
    public void genericApplicationEvent_isNotInterview() {
        LocalDateTime dt = LocalDateTime.of(2026, 9, 5, 9, 0);
        ApplicationEvent generic = new ApplicationEvent("Conference room", dt) {
        };
        assertFalse(generic instanceof Interview);
    }

    // ── OnlineAssessment model data (retained from original test) ─────────────

    @Test
    public void onlineAssessment_fullConstructor_returnsCorrectFields() {
        LocalDateTime dt = LocalDateTime.of(2026, 7, 20, 14, 30);
        OnlineAssessment oa = new OnlineAssessment(
                "Zoom call", dt, "HackerRank", "https://hr.com/test", "Bring passport");

        assertEquals("Zoom call", oa.getLocation());
        assertEquals(dt, oa.getLocalDate());
        assertEquals("HackerRank", oa.getPlatform());
        assertEquals("https://hr.com/test", oa.getLink());
    }

    @Test
    public void onlineAssessment_noNotesConstructor_usesEmptyNotesDefault() {
        LocalDateTime dt = LocalDateTime.of(2026, 8, 10, 10, 0);
        // should not throw
        OnlineAssessment oa = new OnlineAssessment("Office", dt, "Codility", "https://c.com");
        assertEquals("Codility", oa.getPlatform());
    }
}
