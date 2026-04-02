package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class UserPrefsTest {

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        UserPrefs userPref = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPref.setGuiSettings(null));
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        UserPrefs userPrefs = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPrefs.setAddressBookFilePath(null));
    }

    @Test
    public void isReminderHighlightEnabled_defaultFalse() {
        UserPrefs userPrefs = new UserPrefs();
        assertFalse(userPrefs.isReminderHighlightEnabled());
    }

    @Test
    public void setReminderHighlightEnabled_setTrue_success() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setReminderHighlightEnabled(true);
        assertTrue(userPrefs.isReminderHighlightEnabled());
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        UserPrefs userPrefs = new UserPrefs();
        assertEquals(true, userPrefs.equals(userPrefs));
    }

    @Test
    public void equals_null_returnsFalse() {
        UserPrefs userPrefs = new UserPrefs();
        assertFalse(userPrefs.equals(null));
    }

    @Test
    public void equals_differentReminderHighlightEnabled_returnsFalse() {
        UserPrefs withReminderEnabled = new UserPrefs();
        withReminderEnabled.setReminderHighlightEnabled(true);

        UserPrefs withoutReminderEnabled = new UserPrefs();

        assertFalse(withReminderEnabled.equals(withoutReminderEnabled));
    }

    @Test
    public void hashCode_sameValues_sameHash() {
        UserPrefs userPrefs1 = new UserPrefs();
        userPrefs1.setReminderHighlightEnabled(true);

        UserPrefs userPrefs2 = new UserPrefs();
        userPrefs2.setReminderHighlightEnabled(true);

        assertEquals(userPrefs1.hashCode(), userPrefs2.hashCode());
    }
}
