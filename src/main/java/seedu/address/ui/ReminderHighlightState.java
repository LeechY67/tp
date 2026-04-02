package seedu.address.ui;

/**
 * Global toggle for whether {@code reminder} highlighting should be shown.
 * <p>
 * Default is disabled so deadlines added/edited before the first {@code reminder}
 * do not immediately affect the UI highlight colors.
 */
public final class ReminderHighlightState {

    private static boolean enabled = false;

    private ReminderHighlightState() {}

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean enabled) {
        ReminderHighlightState.enabled = enabled;
    }
}

