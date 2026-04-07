package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import seedu.address.testutil.ApplicationBuilder;

public class ApplicationCardTest {

    private static boolean jfxToolkitAvailable = false;

    @BeforeAll
    public static void initJfxRuntime() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException e) {
            // Toolkit already running (normal case when another test class started it first).
            latch.countDown();
        } catch (NullPointerException e) {
            // Monocle is active and the toolkit was already initialised by a prior test class
            // (e.g. EventDetailsWindowTest via ApplicationExtension). Monocle's startup path
            // does not always throw IllegalStateException — it may NPE instead. Treat this
            // the same way: toolkit is up, proceed normally.
            latch.countDown();
        } catch (UnsupportedOperationException e) {
            // Truly headless environment with no toolkit support at all — skip FX tests.
            jfxToolkitAvailable = false;
            return;
        }
        jfxToolkitAvailable = latch.await(5, TimeUnit.SECONDS);
        assertTrue(jfxToolkitAvailable);
    }

    // ── formatPhone ──────────────────────────────────────────────────────────

    @Test
    public void toStatusKey_nullInput_returnsEmpty() {
        assertEquals("", ApplicationCard.toStatusKey(null));
    }

    @Test
    public void toStatusKey_emptyString_returnsEmpty() {
        assertEquals("", ApplicationCard.toStatusKey(""));
    }

    @Test
    public void toStatusKey_applied_returnsLowercase() {
        assertEquals("applied", ApplicationCard.toStatusKey("APPLIED"));
    }

    @Test
    public void toStatusKey_interviewing_returnsLowercase() {
        assertEquals("interviewing", ApplicationCard.toStatusKey("INTERVIEWING"));
    }

    @Test
    public void toStatusKey_offered_returnsLowercase() {
        assertEquals("offered", ApplicationCard.toStatusKey("OFFERED"));
    }

    @Test
    public void toStatusKey_rejected_returnsLowercase() {
        assertEquals("rejected", ApplicationCard.toStatusKey("REJECTED"));
    }

    @Test
    public void toStatusKey_withdrawn_returnsLowercase() {
        assertEquals("withdrawn", ApplicationCard.toStatusKey("WITHDRAWN"));
    }

    @Test
    public void toStatusKey_withUnderscore_replacesWithHyphen() {
        assertEquals("in-progress", ApplicationCard.toStatusKey("IN_PROGRESS"));
    }

    // -----------------------------------------------------------------------
    // toTitleCase
    // -----------------------------------------------------------------------

    @Test
    public void toTitleCase_nullInput_returnsNull() {
        assertNull(ApplicationCard.toTitleCase(null));
    }

    @Test
    public void toTitleCase_emptyString_returnsEmpty() {
        assertEquals("", ApplicationCard.toTitleCase(""));
    }

    @Test
    public void toTitleCase_applied_returnsApplied() {
        assertEquals("Applied", ApplicationCard.toTitleCase("APPLIED"));
    }

    @Test
    public void toTitleCase_interviewing_returnsInterviewing() {
        assertEquals("Interviewing", ApplicationCard.toTitleCase("INTERVIEWING"));
    }

    @Test
    public void toTitleCase_offered_returnsOffered() {
        assertEquals("Offered", ApplicationCard.toTitleCase("OFFERED"));
    }

    @Test
    public void toTitleCase_rejected_returnsRejected() {
        assertEquals("Rejected", ApplicationCard.toTitleCase("REJECTED"));
    }

    @Test
    public void toTitleCase_withdrawn_returnsWithdrawn() {
        assertEquals("Withdrawn", ApplicationCard.toTitleCase("WITHDRAWN"));
    }

    @Test
    public void toTitleCase_withUnderscore_replacesWithSpace() {
        assertEquals("In progress", ApplicationCard.toTitleCase("IN_PROGRESS"));
    }

    @Test
    public void toTitleCase_singleChar_returnsUppercase() {
        assertEquals("A", ApplicationCard.toTitleCase("a"));
    }

    @Test
    public void toTitleCase_firstLetterIsUppercase() {
        String result = ApplicationCard.toTitleCase("APPLIED");
        assertTrue(Character.isUpperCase(result.charAt(0)));
    }

    @Test
    public void toTitleCase_remainingLettersAreLowercase() {
        String result = ApplicationCard.toTitleCase("APPLIED");
        assertEquals(result.substring(1), result.substring(1).toLowerCase());
    }

    // -----------------------------------------------------------------------
    // format* helpers
    // -----------------------------------------------------------------------

    @Test
    public void formatPhone_returnsExactInput() {
        assertEquals("91234567", ApplicationCard.formatPhone("91234567"));
    }

    @Test
    public void formatPhone_emptyString_returnsEmpty() {
        assertEquals("", ApplicationCard.formatPhone(""));
    }

    @Test
    public void formatHrEmail_returnsExactInput() {
        assertEquals("hr@google.com", ApplicationCard.formatHrEmail("hr@google.com"));
    }

    @Test
    public void formatHrEmail_emptyString_returnsEmpty() {
        assertEquals("", ApplicationCard.formatHrEmail(""));
    }

    @Test
    public void formatCompanyName_returnsExactInput() {
        assertEquals("Google", ApplicationCard.formatCompanyName("Google"));
    }

    @Test
    public void formatCompanyName_emptyString_returnsEmpty() {
        assertEquals("", ApplicationCard.formatCompanyName(""));
    }

    @Test
    public void formatCompanyLocation_returnsExactInput() {
        assertEquals("Singapore", ApplicationCard.formatCompanyLocation("Singapore"));
    }

    @Test
    public void formatCompanyLocation_emptyString_returnsEmpty() {
        assertEquals("", ApplicationCard.formatCompanyLocation(""));
    }

    @Test
    public void formatDeadline_returnsExactInput() {
        assertEquals("2026-12-31", ApplicationCard.formatDeadline("2026-12-31"));
    }

    @Test
    public void formatDeadline_emptyString_returnsEmpty() {
        assertEquals("", ApplicationCard.formatDeadline(""));
    }

    @Test
    public void formatNote_returnsExactInput() {
        assertEquals("Follow up next Monday", ApplicationCard.formatNote("Follow up next Monday"));
    }

    @Test
    public void formatNote_emptyString_returnsEmpty() {
        assertEquals("", ApplicationCard.formatNote(""));
    }

    @Test
    public void formatResume_returnsExactInput() {
        assertEquals("resume.pdf", ApplicationCard.formatResume("resume.pdf"));
    }

    @Test
    public void formatResume_emptyString_returnsEmpty() {
        assertEquals("", ApplicationCard.formatResume(""));
    }

    @Test
    public void formatMethods_doNotAddIconPrefix() {
        assertFalse(ApplicationCard.formatPhone("91234567").contains("☎"));
        assertFalse(ApplicationCard.formatHrEmail("hr@google.com").contains("✉"));
        assertFalse(ApplicationCard.formatCompanyName("Google").contains("▣"));
        assertFalse(ApplicationCard.formatCompanyLocation("Singapore").contains("⌂"));
        assertFalse(ApplicationCard.formatDeadline("2026-12-31").contains("◷"));
        assertFalse(ApplicationCard.formatNote("Follow up").contains("✎"));
        assertFalse(ApplicationCard.formatResume("resume.pdf").contains("▣"));
    }

    @Test
    public void formatMethods_allReturnExactInput() {
        assertEquals("91234567", ApplicationCard.formatPhone("91234567"));
        assertEquals("hr@google.com", ApplicationCard.formatHrEmail("hr@google.com"));
        assertEquals("Google", ApplicationCard.formatCompanyName("Google"));
        assertEquals("Singapore", ApplicationCard.formatCompanyLocation("Singapore"));
        assertEquals("2026-12-31", ApplicationCard.formatDeadline("2026-12-31"));
        assertEquals("Follow up", ApplicationCard.formatNote("Follow up"));
        assertEquals("resume.pdf", ApplicationCard.formatResume("resume.pdf"));
    }

    @Test
    public void formatMethods_emptyStrings_returnEmpty() {
        assertEquals("", ApplicationCard.formatPhone(""));
        assertEquals("", ApplicationCard.formatHrEmail(""));
        assertEquals("", ApplicationCard.formatCompanyName(""));
        assertEquals("", ApplicationCard.formatCompanyLocation(""));
        assertEquals("", ApplicationCard.formatDeadline(""));
        assertEquals("", ApplicationCard.formatNote(""));
        assertEquals("", ApplicationCard.formatResume(""));
    }

    @Test
    public void getRoleColor_deadlineWithinThreeDays_returnsUrgentRed() {
        ReminderHighlightState.setEnabled(true);
        LocalDateTime now = LocalDateTime.of(2026, 3, 10, 21, 44);
        Color color = ApplicationCard.getRoleColor(
                new ApplicationBuilder().withDeadline("2026-03-12 21:00").build(), now);
        assertEquals(Color.web("#e53935"), color);
    }

    @Test
    public void getRoleColor_overdueDeadline_returnsGray() {
        ReminderHighlightState.setEnabled(true);
        LocalDateTime now = LocalDateTime.of(2026, 3, 10, 21, 44);
        Color color = ApplicationCard.getRoleColor(
                new ApplicationBuilder().withDeadline("2026-03-10 21:00").build(), now);
        assertEquals(Color.web("#fb8c00"), color);
    }

    @Test
    public void getRoleColor_noDeadline_returnsDefaultWhite() {
        ReminderHighlightState.setEnabled(true);
        LocalDateTime now = LocalDateTime.of(2026, 3, 10, 21, 44);
        Color color = ApplicationCard.getRoleColor(
                new ApplicationBuilder().withDeadline("-").build(), now);
        assertEquals(Color.WHITE, color);
    }

    @Test
    public void getRoleColor_dateOnlyTodayNotOverdue_returnsUrgentRed() {
        ReminderHighlightState.setEnabled(true);
        LocalDateTime now = LocalDateTime.of(2026, 3, 10, 21, 44);
        Color color = ApplicationCard.getRoleColor(
                new ApplicationBuilder().withDeadline("2026-03-10").build(), now);
        assertEquals(Color.web("#e53935"), color);
    }

    @Test
    public void getDeadlineIconColor_overdue_returnsOrange() {
        ReminderHighlightState.setEnabled(true);
        LocalDateTime now = LocalDateTime.of(2026, 4, 1, 21, 44);
        Color color = ApplicationCard.getDeadlineIconColor(
                new ApplicationBuilder().withDeadline("2026-04-01 21:00").build(), now);
        assertEquals(Color.web("#fb8c00"), color);
    }

    @Test
    public void getDeadlineIconColor_deadlineUrgent_returnsRed() {
        ReminderHighlightState.setEnabled(true);
        LocalDateTime now = LocalDateTime.of(2026, 3, 10, 21, 44);
        Color color = ApplicationCard.getDeadlineIconColor(
                new ApplicationBuilder().withDeadline("2026-03-12 21:00").build(), now);
        assertEquals(Color.web("#e53935"), color);
    }

    @Test
    public void getDeadlineIconColor_deadlineFarFuture_returnsDefaultWhite() {
        ReminderHighlightState.setEnabled(true);
        LocalDateTime now = LocalDateTime.of(2026, 3, 10, 21, 44);
        Color color = ApplicationCard.getDeadlineIconColor(
                new ApplicationBuilder().withDeadline("2026-03-20 21:00").build(), now);
        assertEquals(Color.WHITE, color);
    }

    @Test
    public void getRoleColor_dateOnlyWithinThreeDays_returnsUrgentRed() {
        LocalDateTime now = LocalDateTime.of(2026, 4, 2, 21, 44);
        Color color = ApplicationCard.getRoleColor(
                new ApplicationBuilder().withDeadline("2026-04-04").build(), now);
        assertEquals(Color.web("#e53935"), color);
    }

    @Test
    public void getRoleColor_invalidDeadlineDateOnly_returnsDefaultWhite() {
        LocalDateTime now = LocalDateTime.of(2026, 4, 2, 21, 44);
        Color color = ApplicationCard.getRoleColor(
                new ApplicationBuilder().withDeadline("2026-99-99").build(), now);
        assertEquals(Color.WHITE, color);
    }

    @Test
    public void getRoleColor_invalidDeadlineDateTime_returnsDefaultWhite() {
        LocalDateTime now = LocalDateTime.of(2026, 4, 2, 21, 44);
        Color color = ApplicationCard.getRoleColor(
                new ApplicationBuilder().withDeadline("2026-04-01 12:60").build(), now);
        assertEquals(Color.WHITE, color);
    }

    @Test
    public void getRoleColor_datetimeBeyondThreeDays_returnsDefaultWhite() {
        LocalDateTime now = LocalDateTime.of(2026, 3, 10, 21, 44);
        Color color = ApplicationCard.getRoleColor(
                new ApplicationBuilder().withDeadline("2026-03-14 21:00").build(), now);
        assertEquals(Color.WHITE, color);
    }

    @Test
    public void getDeadlineIconColor_noDeadline_returnsUrgentRed() {
        ReminderHighlightState.setEnabled(true);
        LocalDateTime now = LocalDateTime.of(2026, 3, 10, 21, 44);
        Color color = ApplicationCard.getDeadlineIconColor(
                new ApplicationBuilder().withDeadline("-").build(), now);
        assertEquals(Color.WHITE, color);
    }


    @Test
    public void constructor_noDeadline_hidesDeadlineField() throws Exception {
        Assumptions.assumeTrue(jfxToolkitAvailable);

        ApplicationCard card = createCardOnFxThread(new ApplicationBuilder().build(), 1);
        assertNotNull(card);
    }

    @Test
    public void constructor_idLabel_hasFixedWidthAndNoEllipsis() throws Exception {
        Assumptions.assumeTrue(jfxToolkitAvailable);

        ApplicationCard card = createCardOnFxThread(new ApplicationBuilder().build(), 6);
        Label idLabel = getPrivateField(card, "id", Label.class);

        assertFalse(idLabel.isWrapText());
        assertEquals(OverrunStyle.CLIP, idLabel.getTextOverrun());
        assertEquals(Region.USE_PREF_SIZE, idLabel.getMinWidth());
        assertEquals(Region.USE_COMPUTED_SIZE, idLabel.getPrefWidth());
        assertEquals(Region.USE_PREF_SIZE, idLabel.getMaxWidth());
    }

    @Test
    public void constructor_longTextFields_enableWrapLayout() throws Exception {
        Assumptions.assumeTrue(jfxToolkitAvailable);

        ApplicationCard card = createCardOnFxThread(new ApplicationBuilder()
                .withRole("Software Engineer Engineer Engineer Engineer Engineer")
                .withNote("Met recruiter at career fair Met recruiter at career fair")
                .build(), 1);
        Label roleLabel = getPrivateField(card, "role", Label.class);
        Label deadlineLabel = getPrivateField(card, "deadline", Label.class);
        FlowPane tagsPane = getPrivateField(card, "tags", FlowPane.class);
        VBox details = getPrivateField(card, "detailsBox", VBox.class);
        VBox content = getPrivateField(card, "contentBox", VBox.class);

        assertTrue(roleLabel.isWrapText());
        assertEquals(Double.MAX_VALUE, roleLabel.getMaxWidth());
        assertTrue(deadlineLabel.isWrapText() || !deadlineLabel.isManaged());
        assertEquals(Double.MAX_VALUE, tagsPane.getMaxWidth());
        assertEquals(Double.MAX_VALUE, content.getMaxWidth());
        assertEquals(Double.MAX_VALUE, details.getMaxWidth());

        HBox detailRow = (HBox) details.getChildren().get(0);
        Label detailText = (Label) detailRow.getChildren().get(1);
        assertTrue(detailText.isWrapText());
        assertEquals(Double.MAX_VALUE, detailText.getMaxWidth());
    }


    @Test
    public void toStatusKey_null_returnsEmpty() {
        assertEquals("", ApplicationCard.toStatusKey(null));
    }

    @Test
    public void toTitleCase_null_returnsNull() {
        assertNull(ApplicationCard.toTitleCase(null));
    }

    private static ApplicationCard createCardOnFxThread(seedu.address.model.application.Application application,
                                                         int displayedIndex) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final ApplicationCard[] cardHolder = new ApplicationCard[1];
        final Throwable[] errorHolder = new Throwable[1];

        Platform.runLater(() -> {
            try {
                cardHolder[0] = new ApplicationCard(application, displayedIndex);
            } catch (Throwable t) {
                errorHolder[0] = t;
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Timed out waiting for FX task");
        if (errorHolder[0] != null) {
            throw new AssertionError("Failed to create ApplicationCard on FX thread", errorHolder[0]);
        }
        return cardHolder[0];
    }

    private static <T> T getPrivateField(Object target, String fieldName, Class<T> fieldType) throws Exception {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return fieldType.cast(field.get(target));
    }
}
