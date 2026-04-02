package seedu.address;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.storage.UserPrefsStorage;
import seedu.address.ui.ReminderHighlightState;

public class MainAppTest {

    @Test
    public void initPrefs_userPrefsPresent_setsReminderHighlightEnabled() throws Exception {
        ReminderHighlightState.setEnabled(false);
        MainApp mainApp = new MainApp();

        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setReminderHighlightEnabled(true);

        UserPrefsStorage storage = new UserPrefsStorage() {
            @Override
            public Path getUserPrefsFilePath() {
                return Path.of("data", "userPrefs.json");
            }

            @Override
            public Optional<UserPrefs> readUserPrefs() {
                return Optional.of(userPrefs);
            }

            @Override
            public void saveUserPrefs(ReadOnlyUserPrefs userPrefsToSave) {
                // no-op
            }
        };

        mainApp.initPrefs(storage);
        assertTrue(ReminderHighlightState.isEnabled());
    }

    @Test
    public void initPrefs_saveUserPrefsThrowsIoException_stillSetsReminderHighlightEnabled() {
        ReminderHighlightState.setEnabled(false);
        MainApp mainApp = new MainApp();

        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setReminderHighlightEnabled(true);

        UserPrefsStorage storage = new UserPrefsStorage() {
            @Override
            public Path getUserPrefsFilePath() {
                return Path.of("data", "userPrefs.json");
            }

            @Override
            public Optional<UserPrefs> readUserPrefs() {
                return Optional.of(userPrefs);
            }

            @Override
            public void saveUserPrefs(ReadOnlyUserPrefs userPrefsToSave) throws IOException {
                throw new IOException("save failed");
            }
        };

        mainApp.initPrefs(storage);
        assertTrue(ReminderHighlightState.isEnabled());
    }

    @Test
    public void initPrefs_readUserPrefsThrowsDataLoadingException_usesDefaultPrefs() {
        ReminderHighlightState.setEnabled(true);
        MainApp mainApp = new MainApp();

        UserPrefsStorage storage = new UserPrefsStorage() {
            @Override
            public Path getUserPrefsFilePath() {
                return Path.of("data", "userPrefs.json");
            }

            @Override
            public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
                throw new DataLoadingException(new Exception("read failed"));
            }

            @Override
            public void saveUserPrefs(ReadOnlyUserPrefs userPrefsToSave) {
                // no-op
            }
        };

        mainApp.initPrefs(storage);
        assertFalse(ReminderHighlightState.isEnabled());
    }
}

