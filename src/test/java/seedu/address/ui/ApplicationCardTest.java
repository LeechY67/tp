package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import seedu.address.model.application.Application;
import seedu.address.model.application.OnlineAssessment;
import seedu.address.model.application.Status;
import seedu.address.testutil.ApplicationBuilder;


public class ApplicationCardTest {

    @BeforeAll
    public static void initJfxRuntime() throws Exception {
        System.setProperty("prism.order", "sw");
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");

        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException e) {
            latch.countDown();
        }
        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    // -----------------------------------------------------------------------
    // FX-thread helper
    // -----------------------------------------------------------------------

    @FunctionalInterface
    private interface FxTask {
        void run() throws Exception;
    }

    /**
     * Runs {@code task} on the JavaFX Application Thread, waits for it to finish,
     * and re-throws any exception on the test thread.
     */
    private static void onFx(FxTask task) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> error = new AtomicReference<>();
        Platform.runLater(() -> {
            try {
                task.run();
            } catch (Throwable t) {
                error.set(t);
            } finally {
                latch.countDown();
            }
        });
        assertTrue(latch.await(10, TimeUnit.SECONDS), "FX thread timed out");
        if (error.get() != null) {
            throw new RuntimeException(error.get());
        }
    }

    /**
     * Builds an {@link ApplicationCard} on the FX thread and returns it.
     */
    private static ApplicationCard buildCard(Application app, int index) throws Exception {
        AtomicReference<ApplicationCard> ref = new AtomicReference<>();
        onFx(() -> ref.set(new ApplicationCard(app, index)));
        return ref.get();
    }

    // -----------------------------------------------------------------------
    // Reflection helpers — safe to call from test thread after FX construction
    // -----------------------------------------------------------------------

    private static Label getLabel(ApplicationCard card, String field) throws Exception {
        Field f = ApplicationCard.class.getDeclaredField(field);
        f.setAccessible(true);
        return (Label) f.get(card);
    }

    private static String getLabelText(ApplicationCard card, String field) throws Exception {
        return getLabel(card, field).getText();
    }

    private static FlowPane getTagsPane(ApplicationCard card) throws Exception {
        Field f = ApplicationCard.class.getDeclaredField("tags");
        f.setAccessible(true);
        return (FlowPane) f.get(card);
    }

    private static VBox getDetailsBox(ApplicationCard card) throws Exception {
        Field f = ApplicationCard.class.getDeclaredField("detailsBox");
        f.setAccessible(true);
        return (VBox) f.get(card);
    }

    private static Button getEventButton(ApplicationCard card) throws Exception {
        Field f = ApplicationCard.class.getDeclaredField("eventButton");
        f.setAccessible(true);
        return (Button) f.get(card);
    }

    private static EventDetailsWindow getEventDetailsWindow(ApplicationCard card) throws Exception {
        Field f = ApplicationCard.class.getDeclaredField("eventDetailsWindow");
        f.setAccessible(true);
        return (EventDetailsWindow) f.get(card);
    }

    private static Label getStatusTag(ApplicationCard card, String text) throws Exception {
        return getTagsPane(card).getChildren().stream()
                .map(n -> (Label) n)
                .filter(l -> l.getText().equals(text))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Status tag not found: " + text));
    }

    // -----------------------------------------------------------------------
    // Constructor — id and role labels
    // -----------------------------------------------------------------------

    @Test
    public void constructor_setsIdLabel() throws Exception {
        ApplicationCard card = buildCard(new ApplicationBuilder().build(), 3);
        assertEquals("3. ", getLabelText(card, "id"));
    }

    @Test
    public void constructor_setsRoleLabel() throws Exception {
        ApplicationCard card = buildCard(new ApplicationBuilder().withRole("Software Engineer").build(), 1);
        assertEquals("Software Engineer", getLabelText(card, "role"));
    }

    // -----------------------------------------------------------------------
    // Constructor — deadline branch
    // -----------------------------------------------------------------------

    @Test
    public void constructor_withEmptyDeadline_deadlineLabelHidden() throws Exception {
        ApplicationCard card = buildCard(new ApplicationBuilder().build(), 1);
        Label dl = getLabel(card, "deadline");
        assertFalse(dl.isVisible(), "Deadline label should be hidden when deadline is empty");
        assertFalse(dl.isManaged(), "Deadline label should be unmanaged when deadline is empty");
    }

    @Test
    public void constructor_withDeadline_deadlineLabelShowsValue() throws Exception {
        Application app = new ApplicationBuilder().withDeadline("2026-12-31").build();
        ApplicationCard card = buildCard(app, 1);
        Label dl = getLabel(card, "deadline");
        assertTrue(dl.isVisible(), "Deadline label should be visible when deadline is set");
        assertTrue(dl.isManaged(), "Deadline label should be managed when deadline is set");
        assertEquals("2026-12-31", dl.getText());
        // calendar icon must be set
        assertNotNull(dl.getGraphic(), "Deadline label should have a calendar graphic");
    }

    // -----------------------------------------------------------------------
    // Constructor — company location branch
    // -----------------------------------------------------------------------

    @Test
    public void constructor_withCompanyLocation_locationRowPresent() throws Exception {
        // Default ApplicationBuilder includes a non-empty location ("Singapore")
        ApplicationCard card = buildCard(new ApplicationBuilder().build(), 1);
        VBox box = getDetailsBox(card);
        // company name row + location row + phone + email = at least 4 rows
        assertTrue(box.getChildren().size() >= 4,
                "detailsBox should contain at least 4 rows when company location is set");
    }

    @Test
    public void constructor_withEmptyCompanyLocation_noLocationRow() throws Exception {
        Application app = new ApplicationBuilder().withCompany("Acme").build(); // clears location
        ApplicationCard card = buildCard(app, 1);
        VBox box = getDetailsBox(card);
        // company name + phone + email = 3 rows (no location row)
        assertEquals(3, box.getChildren().size(),
                "detailsBox should have exactly 3 rows when company location is empty");
    }

    // -----------------------------------------------------------------------
    // Constructor — note branch
    // -----------------------------------------------------------------------

    @Test
    public void constructor_withNote_noteRowPresent() throws Exception {
        Application app = new ApplicationBuilder().withNote("Follow up on Monday").build();
        ApplicationCard card = buildCard(app, 1);
        VBox box = getDetailsBox(card);
        // company + location + phone + email + note = 5 rows
        assertEquals(5, box.getChildren().size(),
                "detailsBox should include a note row when note is non-empty");
    }

    @Test
    public void constructor_withEmptyNote_noteRowAbsent() throws Exception {
        Application app = new ApplicationBuilder().withNote("").build();
        ApplicationCard card = buildCard(app, 1);
        VBox box = getDetailsBox(card);
        // company + location + phone + email = 4 rows (no note)
        assertEquals(4, box.getChildren().size(),
                "detailsBox should not include a note row when note is empty");
    }


    // -----------------------------------------------------------------------
    // Tags — status chip and reminder chip
    // -----------------------------------------------------------------------

    @Test
    public void constructor_statusChip_isAddedToTagsPane() throws Exception {
        Application app = new ApplicationBuilder().withStatus(Status.APPLIED).build();
        ApplicationCard card = buildCard(app, 1);
        // Must not throw — the status chip labelled "Applied" must exist
        Label chip = getStatusTag(card, "Applied");
        assertNotNull(chip);
    }

    @Test
    public void constructor_nonReminderTag_doesNotGetUrgentStyleClass() throws Exception {
        Application app = new ApplicationBuilder().withTags("Tech").build();
        ApplicationCard card = buildCard(app, 1);
        FlowPane pane = getTagsPane(card);
        // The "Tech" chip must NOT have chip-urgent
        boolean techChipIsUrgent = pane.getChildren().stream()
                .map(n -> (Label) n)
                .filter(l -> l.getText().equals("Tech"))
                .anyMatch(l -> l.getStyleClass().contains("chip-urgent"));
        assertFalse(techChipIsUrgent, "Non-reminder tag chip should not carry the chip-urgent CSS class");
    }

    // -----------------------------------------------------------------------
    // Event button coverage
    // -----------------------------------------------------------------------

    @Test
    public void constructor_withNoEvent_eventButtonHidden() throws Exception {
        ApplicationCard card = buildCard(new ApplicationBuilder().build(), 1);

        Button btn = getEventButton(card);
        assertFalse(btn.isVisible(), "Event button should be hidden when there is no ApplicationEvent");
        assertFalse(btn.isManaged(), "Event button should be unmanaged when there is no ApplicationEvent");
    }

    @Test
    public void constructor_withEvent_eventButtonVisible() throws Exception {
        OnlineAssessment event = new OnlineAssessment(
                "Zoom", LocalDateTime.of(2026, 6, 15, 10, 0),
                "HackerRank", "https://hr.com", "Bring ID");
        Application base = new ApplicationBuilder().build();
        Application app = new Application(
                base.getRole(), base.getPhone(), base.getHrEmail(), base.getCompany(),
                base.getTags(), base.getStatus(), base.getDeadline(),
                event, base.getNote(), base.getResume());
        ApplicationCard card = buildCard(app, 1);

        Button btn = getEventButton(card);
        assertTrue(btn.isVisible(), "Event button should be visible when an ApplicationEvent is present");
        assertTrue(btn.isManaged(), "Event button should be managed when an ApplicationEvent is present");
    }

    /**
     * When the EventDetailsWindow is not yet showing, clicking the event button
     * should call {@code show()} without throwing.
     */
    @Test
    public void handleEventButtonClick_whenWindowNotShowing_callsShow() throws Exception {
        OnlineAssessment event = new OnlineAssessment(
                "Online", LocalDateTime.of(2026, 8, 1, 9, 0),
                "Codility", "https://codility.com");
        Application base = new ApplicationBuilder().build();
        Application app = new Application(
                base.getRole(), base.getPhone(), base.getHrEmail(), base.getCompany(),
                base.getTags(), base.getStatus(), base.getDeadline(),
                event, base.getNote(), base.getResume());

        // Build card AND invoke the handler — both on the FX thread
        onFx(() -> {
            ApplicationCard card = new ApplicationCard(app, 1);
            assertFalse(getEventDetailsWindow(card).isShowing());

            Method handler = ApplicationCard.class.getDeclaredMethod("handleEventButtonClick");
            handler.setAccessible(true);
            handler.invoke(card); // must not throw; calls show()
        });
    }

    /**
     * When the EventDetailsWindow is already showing, clicking the event button
     * should call {@code focus()} instead of {@code show()}.
     * A stub window is injected via reflection to force {@code isShowing() == true}.
     */
    @Test
    public void handleEventButtonClick_whenWindowAlreadyShowing_callsFocus() throws Exception {
        OnlineAssessment event = new OnlineAssessment(
                "Online", LocalDateTime.of(2026, 9, 1, 14, 0),
                "LeetCode", "https://leetcode.com");
        Application base = new ApplicationBuilder().build();
        Application app = new Application(
                base.getRole(), base.getPhone(), base.getHrEmail(), base.getCompany(),
                base.getTags(), base.getStatus(), base.getDeadline(),
                event, base.getNote(), base.getResume());

        onFx(() -> {
            ApplicationCard card = new ApplicationCard(app, 1);

            // Stub that always reports isShowing() == true so focus() branch is taken
            EventDetailsWindow stub = new EventDetailsWindow() {
                @Override
                public boolean isShowing() {
                    return true;
                }

                @Override
                public void focus() {
                    // intentionally empty — we just verify this path is reached without error
                }
            };
            stub.setEventDetails(event);

            Field edwField = ApplicationCard.class.getDeclaredField("eventDetailsWindow");
            edwField.setAccessible(true);
            edwField.set(card, stub);

            Method handler = ApplicationCard.class.getDeclaredMethod("handleEventButtonClick");
            handler.setAccessible(true);
            handler.invoke(card); // must not throw; calls focus()
        });
    }

    // -----------------------------------------------------------------------
    // format* helpers
    // -----------------------------------------------------------------------

    @Test
    public void formatPhone_normalValue_returnsValue() {
        assertEquals("91234567", ApplicationCard.formatPhone("91234567"));
    }

    @Test
    public void formatPhone_emptyString_returnsEmpty() {
        assertEquals("", ApplicationCard.formatPhone(""));
    }

    @Test
    public void formatHrEmail_normalValue_returnsValue() {
        assertEquals("hr@google.com", ApplicationCard.formatHrEmail("hr@google.com"));
    }

    @Test
    public void formatHrEmail_emptyString_returnsEmpty() {
        assertEquals("", ApplicationCard.formatHrEmail(""));
    }

    @Test
    public void formatCompanyName_normalValue_returnsValue() {
        assertEquals("Google", ApplicationCard.formatCompanyName("Google"));
    }

    @Test
    public void formatCompanyName_emptyString_returnsEmpty() {
        assertEquals("", ApplicationCard.formatCompanyName(""));
    }

    @Test
    public void formatCompanyLocation_normalValue_returnsValue() {
        assertEquals("Singapore", ApplicationCard.formatCompanyLocation("Singapore"));
    }

    @Test
    public void formatCompanyLocation_emptyString_returnsEmpty() {
        assertEquals("", ApplicationCard.formatCompanyLocation(""));
    }

    @Test
    public void formatDeadline_normalValue_returnsValue() {
        assertEquals("2026-12-31", ApplicationCard.formatDeadline("2026-12-31"));
    }

    @Test
    public void formatDeadline_emptyString_returnsEmpty() {
        assertEquals("", ApplicationCard.formatDeadline(""));
    }

    @Test
    public void formatNote_normalValue_returnsValue() {
        assertEquals("Follow up next Monday", ApplicationCard.formatNote("Follow up next Monday"));
    }

    @Test
    public void formatNote_emptyString_returnsEmpty() {
        assertEquals("", ApplicationCard.formatNote(""));
    }

    @Test
    public void formatResume_normalValue_returnsValue() {
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

    // -----------------------------------------------------------------------
    // toStatusKey
    // -----------------------------------------------------------------------

    @Test
    public void toStatusKey_nullInput_returnsEmpty() {
        assertEquals("", ApplicationCard.toStatusKey(null));
    }

    @Test
    public void toStatusKey_emptyString_returnsEmpty() {
        assertEquals("", ApplicationCard.toStatusKey(""));
    }

    @Test
    public void toStatusKey_applied_returnsApplied() {
        assertEquals("applied", ApplicationCard.toStatusKey("APPLIED"));
    }

    @Test
    public void toStatusKey_interviewing_returnsInterviewing() {
        assertEquals("interviewing", ApplicationCard.toStatusKey("INTERVIEWING"));
    }

    @Test
    public void toStatusKey_offered_returnsOffered() {
        assertEquals("offered", ApplicationCard.toStatusKey("OFFERED"));
    }

    @Test
    public void toStatusKey_rejected_returnsRejected() {
        assertEquals("rejected", ApplicationCard.toStatusKey("REJECTED"));
    }

    @Test
    public void toStatusKey_withdrawn_returnsWithdrawn() {
        assertEquals("withdrawn", ApplicationCard.toStatusKey("WITHDRAWN"));
    }

    @Test
    public void toStatusKey_withUnderscore_replaceWithHyphen() {
        assertEquals("in-progress", ApplicationCard.toStatusKey("IN_PROGRESS"));
    }

    // -----------------------------------------------------------------------
    // toTitleCase
    // -----------------------------------------------------------------------

    @Test
    public void toTitleCase_nullInput_returnsNull() {
        // null guard: the method returns the input unchanged when it is null
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
        String rest = result.substring(1);
        assertEquals(rest, rest.toLowerCase());
    }
}
