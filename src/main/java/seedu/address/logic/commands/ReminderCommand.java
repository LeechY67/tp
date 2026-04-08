package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import seedu.address.model.Model;
import seedu.address.model.application.Application;

/**
 * Sorts job applications by deadline so users can quickly review urgent items.
 */
public class ReminderCommand extends Command {
    public static final String COMMAND_WORD = "reminder";
    public static final String MESSAGE_SUCCESS = "Sorted by deadline and refreshed reminder highlighting!";
    private static final java.util.logging.Logger logger =
            seedu.address.commons.core.LogsCenter.getLogger(ReminderCommand.class);

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        logger.info("Starting reminder command execution...");
        boolean wasReminderEnabled = model.getUserPrefs().isReminderHighlightEnabled();
        List<Application> beforeSortOrder = new ArrayList<>(model.getAddressBook().getApplicationList());

        seedu.address.ui.ReminderHighlightState.setEnabled(true);

        // Persist the toggle in user preferences so the highlighting remains across app restarts.
        seedu.address.model.UserPrefs updatedPrefs = new seedu.address.model.UserPrefs(model.getUserPrefs());
        updatedPrefs.setReminderHighlightEnabled(true);
        model.setUserPrefs(updatedPrefs);

        Comparator<Application> comparator = (a1, a2) ->
                a1.getDeadline().compareTo(a2.getDeadline());
        model.updateSortedApplicationList(comparator);
        List<Application> afterSortOrder = new ArrayList<>(model.getAddressBook().getApplicationList());
        boolean sortOrderChanged = !beforeSortOrder.equals(afterSortOrder);

        if (!wasReminderEnabled || sortOrderChanged) {
            model.commitAddressBook();
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
