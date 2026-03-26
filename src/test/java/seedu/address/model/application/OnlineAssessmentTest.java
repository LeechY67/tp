package seedu.address.model.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class OnlineAssessmentTest {

    private static final LocalDateTime VALID_DATETIME = LocalDateTime.of(2026, 12, 31, 23, 59);
    private static final LocalDateTime ANOTHER_DATETIME = LocalDateTime.of(2026, 6, 15, 10, 0);

    @Test
    public void constructor_allFields_success() {
        OnlineAssessment oa = new OnlineAssessment("home", VALID_DATETIME,
                "HackerRank", "www.hackerrank.com", "bring notes");
        assertEquals("home", oa.getLocation());
        assertEquals(VALID_DATETIME, oa.getLocalDate());
        assertEquals("HackerRank", oa.getPlatform());
        assertEquals("www.hackerrank.com", oa.getLink());
        assertEquals("bring notes", oa.getNotes());
    }

    @Test
    public void constructor_withoutNotes_defaultNotesSet() {
        OnlineAssessment oa = new OnlineAssessment("home", VALID_DATETIME,
                "HackerRank", "www.hackerrank.com");
        assertEquals("home", oa.getLocation());
        assertEquals(VALID_DATETIME, oa.getLocalDate());
        assertEquals("HackerRank", oa.getPlatform());
        assertEquals("www.hackerrank.com", oa.getLink());
        assertEquals(OnlineAssessment.EMPTY_NOTES_VALUE, oa.getNotes());
    }

    @Test
    public void constructor_emptyLocation_success() {
        OnlineAssessment oa = new OnlineAssessment("", VALID_DATETIME,
                "LeetCode", "www.leetcode.com");
        assertEquals("", oa.getLocation());
    }

    @Test
    public void getLocation_returnsCorrectLocation() {
        OnlineAssessment oa = new OnlineAssessment("Singapore", VALID_DATETIME,
                "Codility", "www.codility.com");
        assertEquals("Singapore", oa.getLocation());
    }

    @Test
    public void getLocalDate_returnsCorrectDateTime() {
        OnlineAssessment oa = new OnlineAssessment("home", VALID_DATETIME,
                "HackerRank", "www.hackerrank.com");
        assertEquals(VALID_DATETIME, oa.getLocalDate());
    }

    @Test
    public void getLocalDate_differentDateTime_returnsCorrectDateTime() {
        OnlineAssessment oa = new OnlineAssessment("home", ANOTHER_DATETIME,
                "HackerRank", "www.hackerrank.com");
        assertEquals(ANOTHER_DATETIME, oa.getLocalDate());
    }

    @Test
    public void getPlatform_returnsCorrectPlatform() {
        OnlineAssessment oa = new OnlineAssessment("home", VALID_DATETIME,
                "Codility", "www.codility.com");
        assertEquals("Codility", oa.getPlatform());
    }

    @Test
    public void getLink_returnsCorrectLink() {
        OnlineAssessment oa = new OnlineAssessment("home", VALID_DATETIME,
                "Codility", "www.codility.com");
        assertEquals("www.codility.com", oa.getLink());
    }

    @Test
    public void getNotes_customNotes_returnsCorrectNotes() {
        OnlineAssessment oa = new OnlineAssessment("home", VALID_DATETIME,
                "HackerRank", "www.hackerrank.com", "custom notes");
        assertEquals("custom notes", oa.getNotes());
    }

    @Test
    public void emptyNotesValue_isCorrectConstant() {
        assertEquals("No notes set", OnlineAssessment.EMPTY_NOTES_VALUE);
    }

    @Test
    public void isInstanceOfApplicationEvent() {
        OnlineAssessment oa = new OnlineAssessment("home", VALID_DATETIME,
                "HackerRank", "www.hackerrank.com");
        assertTrue(oa instanceof ApplicationEvent);
    }
}
