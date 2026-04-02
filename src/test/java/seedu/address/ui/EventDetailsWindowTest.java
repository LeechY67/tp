package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import seedu.address.model.application.ApplicationEvent;
import seedu.address.model.application.OnlineAssessment;

/**
 * Tests for {@link EventDetailsWindow}.
 *
 * All JavaFX object creation and mutation is executed on the FX Application Thread.
 * FX-dependent tests are skipped in headless / unsupported CI environments.
 */
public class EventDetailsWindowTest {

    private static boolean jfxToolkitAvailable = false;

    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

    @BeforeAll
    public static void initJfxRuntime() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException e) {
            // JavaFX toolkit already initialized.
            latch.countDown();
        } catch (UnsupportedOperationException e) {
            // Headless / unsupported environment.
            jfxToolkitAvailable = false;
            return;
        }
        jfxToolkitAvailable = latch.await(5, TimeUnit.SECONDS);
        assertTrue(jfxToolkitAvailable);
    }

    @FunctionalInterface
    private interface FxTask {
        void run() throws Exception;
    }

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

    private EventDetailsWindow newWindowOnFx() throws Exception {
        AtomicReference<EventDetailsWindow> ref = new AtomicReference<>();
        onFx(() -> ref.set(new EventDetailsWindow()));
        return ref.get();
    }

    private String getLabelText(EventDetailsWindow window, String fieldName) throws Exception {
        Field field = EventDetailsWindow.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return ((Label) field.get(window)).getText();
    }

    @Test
    public void setEventDetails_withOnlineAssessmentNoNotes_showsDefaultNotes() throws Exception {
        Assumptions.assumeTrue(jfxToolkitAvailable);

        EventDetailsWindow window = newWindowOnFx();
        LocalDateTime dt = LocalDateTime.of(2026, 8, 10, 10, 0);
        OnlineAssessment oa = new OnlineAssessment("Office", dt, "Codility", "https://c.com");

        onFx(() -> window.setEventDetails(oa));

        assertEquals("Online Assessment Details", getLabelText(window, "titleLabel"));
        assertEquals("Office", getLabelText(window, "locationLabel"));
        assertEquals(dt.format(DISPLAY_FORMATTER), getLabelText(window, "dateTimeLabel"));
        assertEquals("Codility", getLabelText(window, "platformLabel"));
        assertEquals("https://c.com", getLabelText(window, "linkLabel"));
        assertEquals(OnlineAssessment.EMPTY_NOTES_VALUE, getLabelText(window, "notesLabel"));
    }

    @Test
    public void setEventDetails_withGenericApplicationEvent_populatesFallbackLabels() throws Exception {
        Assumptions.assumeTrue(jfxToolkitAvailable);

        EventDetailsWindow window = newWindowOnFx();
        LocalDateTime dt = LocalDateTime.of(2026, 9, 5, 9, 0);
        ApplicationEvent generic = new ApplicationEvent("Conference room", dt) { };

        onFx(() -> window.setEventDetails(generic));

        assertEquals("Event Details", getLabelText(window, "titleLabel"));
        assertEquals("Conference room", getLabelText(window, "locationLabel"));
        assertEquals(dt.format(DISPLAY_FORMATTER), getLabelText(window, "dateTimeLabel"));
        assertEquals("N/A", getLabelText(window, "platformLabel"));
        assertEquals("N/A", getLabelText(window, "linkLabel"));
        assertEquals("N/A", getLabelText(window, "notesLabel"));
    }

    @Test
    public void handleClose_hidesWindow() throws Exception {
        Assumptions.assumeTrue(jfxToolkitAvailable);

        EventDetailsWindow window = newWindowOnFx();

        onFx(window::show);
        assertTrue(window.isShowing());

        onFx(() -> {
            Method method = EventDetailsWindow.class.getDeclaredMethod("handleClose");
            method.setAccessible(true);
            method.invoke(window);
        });

        assertFalse(window.isShowing());
    }

    @Test
    public void constructor_withStage_usesProvidedStage() throws Exception {
        Assumptions.assumeTrue(jfxToolkitAvailable);

        AtomicReference<Stage> rootRef = new AtomicReference<>();

        onFx(() -> {
            Stage stage = new Stage();
            EventDetailsWindow window = new EventDetailsWindow(stage);
            rootRef.set(window.getRoot());
        });

        assertNotNull(rootRef.get());
    }
}
