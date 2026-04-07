package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import seedu.address.logic.commands.CommandResult;

@DisabledOnOs(OS.LINUX)
public class CommandBoxTest {

    private static boolean jfxToolkitAvailable = false;

    @BeforeAll
    public static void initJfxRuntime() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException e) {
            latch.countDown();
        } catch (UnsupportedOperationException e) {
            jfxToolkitAvailable = false;
            return;
        }
        jfxToolkitAvailable = latch.await(5, TimeUnit.SECONDS);
        assertTrue(jfxToolkitAvailable);
    }

    @Test
    public void commandTextField_setMultiLineText_newlineFilteredToSingleLine() throws Exception {
        Assumptions.assumeTrue(jfxToolkitAvailable);
        CommandBox commandBox = runOnFxAndWait(() -> new CommandBox(command -> new CommandResult("ok")));
        TextArea commandTextArea = getCommandTextArea(commandBox);

        runOnFxAndWait(() -> {
            commandTextArea.setText("line1\nline2");
            return null;
        });

        String text = runOnFxAndWait(commandTextArea::getText);
        assertEquals("", text);
    }

    @Test
    public void commandTextField_pressEnter_executesCommandAndClearsInput() throws Exception {
        Assumptions.assumeTrue(jfxToolkitAvailable);
        AtomicReference<String> executedCommand = new AtomicReference<>();
        CommandBox commandBox = runOnFxAndWait(() -> new CommandBox(command -> {
            executedCommand.set(command);
            return new CommandResult("ok");
        }));
        TextArea commandTextArea = getCommandTextArea(commandBox);

        runOnFxAndWait(() -> {
            commandTextArea.setText("list");
            KeyEvent enter = new KeyEvent(
                    KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER, false, false, false, false);
            commandTextArea.fireEvent(enter);
            return null;
        });

        assertEquals("list", executedCommand.get());
        String textAfterExecute = runOnFxAndWait(commandTextArea::getText);
        assertEquals("", textAfterExecute);
    }

    private TextArea getCommandTextArea(CommandBox commandBox) throws Exception {
        Field field = CommandBox.class.getDeclaredField("commandTextField");
        field.setAccessible(true);
        return (TextArea) field.get(commandBox);
    }

    private static <T> T runOnFxAndWait(FxSupplier<T> supplier) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<T> result = new AtomicReference<>();
        AtomicReference<Throwable> throwable = new AtomicReference<>();
        Platform.runLater(() -> {
            try {
                result.set(supplier.get());
            } catch (Throwable t) {
                throwable.set(t);
            } finally {
                latch.countDown();
            }
        });
        assertTrue(latch.await(5, TimeUnit.SECONDS));
        if (throwable.get() != null) {
            throw new RuntimeException(throwable.get());
        }
        return result.get();
    }

    @FunctionalInterface
    private interface FxSupplier<T> {
        T get() throws Exception;
    }
}

