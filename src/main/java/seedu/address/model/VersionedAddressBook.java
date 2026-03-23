package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@code AddressBook} capable of recording its own historical status.
 * It achieves Undo and Redo by maintaining a status list and a pointer.
 */
public class VersionedAddressBook extends AddressBook {
    private static final int MAX_UNDO_LIMIT = 10;

    private final List<ReadOnlyAddressBook> addressBookStateList;
    private int currentStatePointer;

    /**
     * Creates a {@code VersionedAddressBook} with an initial snapshot.
     *
     * @param toBeCopied the initial address book state to be copied.
     */
    public VersionedAddressBook(ReadOnlyAddressBook toBeCopied) {
        super(toBeCopied);
        addressBookStateList = new ArrayList<>();
        addressBookStateList.add(new AddressBook(toBeCopied));
        currentStatePointer = 0;
    }

    /**
     * Save the current snapshot of {@code AddressBook}.
     * If the user performs a new modification operation after executing undo,
     * all "future" states after the original pointer will be cleared.
     */
    public void commit() {
        removeStatesAfterPointer();
        addressBookStateList.add(new AddressBook(this));
        currentStatePointer++;

        if (addressBookStateList.size() > MAX_UNDO_LIMIT) {
            addressBookStateList.remove(0);
            currentStatePointer--;
        }
    }

    /**
     * Restore to the previous saved state (time reversal).
     */
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        currentStatePointer--;
        resetData(addressBookStateList.get(currentStatePointer));
    }

    /**
     * Restore to the state before the revocation (back to the future).
     */
    public void redo() {
        if (!canRedo()) {
            throw new NoRedoableStateException();
        }
        currentStatePointer++;
        resetData(addressBookStateList.get(currentStatePointer));
    }

    /**
     * All redundant states after clearing the current pointer.
     */
    private void removeStatesAfterPointer() {
        addressBookStateList.subList(currentStatePointer + 1, addressBookStateList.size()).clear();
    }

    /**
     * Check whether the revocation can be performed.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Check whether the redo can be performed.
     */
    public boolean canRedo() {
        return currentStatePointer < addressBookStateList.size() - 1;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof VersionedAddressBook)) {
            return false;
        }

        VersionedAddressBook otherVersionedAddressBook = (VersionedAddressBook) other;

        return super.equals(otherVersionedAddressBook)
                && addressBookStateList.equals(otherVersionedAddressBook.addressBookStateList)
                && currentStatePointer == otherVersionedAddressBook.currentStatePointer;
    }

    /**
     * A runtime exception thrown when it cannot be undone (already at the beginning of the list).
     */
    public static class NoUndoableStateException extends RuntimeException {
        private NoUndoableStateException() {
            super("There are no more historical records to be undone");
        }
    }

    /**
     * A runtime exception thrown when it cannot be redone (already at the end of the list).
     */
    public static class NoRedoableStateException extends RuntimeException {
        private NoRedoableStateException() {
            super("There is no operation that can be redone");
        }
    }


}
