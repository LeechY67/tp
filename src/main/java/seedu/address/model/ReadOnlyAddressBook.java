package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.application.Application;

/**
 * Unmodifiable view of an Hired!
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Application> getApplicationList();

}
