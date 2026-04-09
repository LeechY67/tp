package seedu.address.model.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class InterviewTest {

    private static final LocalDateTime VALID_DATETIME = LocalDateTime.of(2026, 12, 31, 23, 59);
    private static final LocalDateTime ANOTHER_DATETIME = LocalDateTime.of(2026, 6, 15, 10, 0);

    // ── constructor: all fields ───────────────────────────────────────────────

    @Test
    public void constructor_allFields_success() {
        Interview iv = new Interview("Google HQ", VALID_DATETIME, "John Doe", "technical");
        assertEquals("Google HQ", iv.getLocation());
        assertEquals(VALID_DATETIME, iv.getLocalDate());
        assertEquals("John Doe", iv.getInterviewerName());
        assertEquals("technical", iv.getInterviewType());
    }

    @Test
    public void constructor_requiredFieldsOnly_defaultsAreEmpty() {
        Interview iv = new Interview("Zoom", VALID_DATETIME);
        assertEquals("Zoom", iv.getLocation());
        assertEquals(VALID_DATETIME, iv.getLocalDate());
        assertEquals(Interview.DEFAULT_INTERVIEWER, iv.getInterviewerName());
        assertEquals(Interview.DEFAULT_INTERVIEW_TYPE, iv.getInterviewType());
        assertEquals("", iv.getInterviewerName());
        assertEquals("", iv.getInterviewType());
    }

    @Test
    public void constructor_nullInterviewerName_defaultsToEmpty() {
        Interview iv = new Interview("Office", VALID_DATETIME, null, "behavioural");
        assertEquals("", iv.getInterviewerName());
        assertEquals("behavioural", iv.getInterviewType());
    }

    @Test
    public void constructor_nullInterviewType_defaultsToEmpty() {
        Interview iv = new Interview("Office", VALID_DATETIME, "Jane Smith", null);
        assertEquals("Jane Smith", iv.getInterviewerName());
        assertEquals("", iv.getInterviewType());
    }

    @Test
    public void constructor_bothOptionalFieldsNull_defaultsToEmpty() {
        Interview iv = new Interview("Remote", VALID_DATETIME, null, null);
        assertEquals("", iv.getInterviewerName());
        assertEquals("", iv.getInterviewType());
    }

    @Test
    public void constructor_emptyLocation_success() {
        Interview iv = new Interview("", VALID_DATETIME, "Alice", "panel");
        assertEquals("", iv.getLocation());
    }

    // ── getters ───────────────────────────────────────────────────────────────

    @Test
    public void getLocation_returnsCorrectLocation() {
        Interview iv = new Interview("Singapore", VALID_DATETIME, "Bob", "technical");
        assertEquals("Singapore", iv.getLocation());
    }

    @Test
    public void getLocalDate_returnsCorrectDateTime() {
        Interview iv = new Interview("home", VALID_DATETIME, "Charlie", "hr");
        assertEquals(VALID_DATETIME, iv.getLocalDate());
    }

    @Test
    public void getLocalDate_differentDateTime_returnsCorrectDateTime() {
        Interview iv = new Interview("home", ANOTHER_DATETIME, "Dave", "technical");
        assertEquals(ANOTHER_DATETIME, iv.getLocalDate());
    }

    @Test
    public void getInterviewerName_returnsCorrectName() {
        Interview iv = new Interview("Office", VALID_DATETIME, "Eve", "case");
        assertEquals("Eve", iv.getInterviewerName());
    }

    @Test
    public void getInterviewType_returnsCorrectType() {
        Interview iv = new Interview("Office", VALID_DATETIME, "Frank", "behavioural");
        assertEquals("behavioural", iv.getInterviewType());
    }

    // ── instanceof / inheritance ──────────────────────────────────────────────

    @Test
    public void isInstanceOfApplicationEvent() {
        Interview iv = new Interview("Office", VALID_DATETIME, "Grace", "technical");
        assertTrue(iv instanceof ApplicationEvent);
    }

    @Test
    public void isInstanceOfInterview() {
        Interview iv = new Interview("Office", VALID_DATETIME);
        assertTrue(iv instanceof Interview);
    }

    // ── equals ────────────────────────────────────────────────────────────────

    @Test
    public void equals_sameFields_returnsTrue() {
        Interview a = new Interview("Google HQ", VALID_DATETIME, "John", "technical");
        Interview b = new Interview("Google HQ", VALID_DATETIME, "John", "technical");
        assertEquals(a, b);
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Interview iv = new Interview("Google HQ", VALID_DATETIME, "John", "technical");
        assertEquals(iv, iv);
    }

    @Test
    public void equals_differentLocation_returnsFalse() {
        Interview a = new Interview("Google HQ", VALID_DATETIME, "John", "technical");
        Interview b = new Interview("Zoom", VALID_DATETIME, "John", "technical");
        assertNotEquals(a, b);
    }

    @Test
    public void equals_differentDateTime_returnsFalse() {
        Interview a = new Interview("Google HQ", VALID_DATETIME, "John", "technical");
        Interview b = new Interview("Google HQ", ANOTHER_DATETIME, "John", "technical");
        assertNotEquals(a, b);
    }

    @Test
    public void equals_differentInterviewerName_returnsFalse() {
        Interview a = new Interview("Google HQ", VALID_DATETIME, "John", "technical");
        Interview b = new Interview("Google HQ", VALID_DATETIME, "Jane", "technical");
        assertNotEquals(a, b);
    }

    @Test
    public void equals_differentInterviewType_returnsFalse() {
        Interview a = new Interview("Google HQ", VALID_DATETIME, "John", "technical");
        Interview b = new Interview("Google HQ", VALID_DATETIME, "John", "behavioural");
        assertNotEquals(a, b);
    }

    @Test
    public void equals_withDefaultOptionalFields_returnsTrue() {
        Interview a = new Interview("Zoom", VALID_DATETIME);
        Interview b = new Interview("Zoom", VALID_DATETIME, "", "");
        assertEquals(a, b);
    }

    @Test
    public void equals_notInterviewObject_returnsFalse() {
        Interview iv = new Interview("Office", VALID_DATETIME, "John", "technical");
        assertNotEquals(iv, "not an interview");
    }

    @Test
    public void equals_null_returnsFalse() {
        Interview iv = new Interview("Office", VALID_DATETIME, "John", "technical");
        assertNotEquals(iv, null);
    }

    // ── toString ──────────────────────────────────────────────────────────────

    @Test
    public void toString_allFields_containsKeyInfo() {
        Interview iv = new Interview("Google HQ", VALID_DATETIME, "John", "technical");
        String str = iv.toString();
        assertTrue(str.contains("Google HQ"));
        assertTrue(str.contains("John"));
        assertTrue(str.contains("technical"));
    }

    @Test
    public void toString_noOptionalFields_doesNotContainInterviewerOrType() {
        Interview iv = new Interview("Zoom", VALID_DATETIME);
        String str = iv.toString();
        assertTrue(str.contains("Zoom"));
        // optional fields empty → should not appear in toString
        assertFalse(str.contains("interviewer="));
        assertFalse(str.contains("type="));
    }
}
