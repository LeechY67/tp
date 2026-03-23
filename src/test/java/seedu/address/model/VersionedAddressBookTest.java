package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalApplications.AMY;
import static seedu.address.testutil.TypicalApplications.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

public class VersionedAddressBookTest {

    private final ReadOnlyAddressBook typicalAddressBook = getTypicalAddressBook();

    @Test
    public void constructor_initialState_stored() {
        VersionedAddressBook vbook = new VersionedAddressBook(typicalAddressBook);
        assertFalse(vbook.canUndo());
        assertFalse(vbook.canRedo());
    }

    @Test
    public void commit_newStateStored_pointerIncremented() {
        VersionedAddressBook vbook = new VersionedAddressBook(typicalAddressBook);

        vbook.addApplication(AMY);
        vbook.commit();

        assertTrue(vbook.canUndo());
        assertFalse(vbook.canRedo());
    }

    @Test
    public void undo_restoresPreviousState() {
        VersionedAddressBook vbook = new VersionedAddressBook(typicalAddressBook);
        vbook.addApplication(AMY);
        vbook.commit();

        vbook.undo();
        assertEquals(typicalAddressBook, new AddressBook(vbook));
    }

    @Test
    public void redo_restoresUndoneState() {
        VersionedAddressBook vbook = new VersionedAddressBook(typicalAddressBook);
        vbook.addApplication(AMY);
        vbook.commit();

        vbook.undo();
        vbook.redo();

        AddressBook expectedBook = new AddressBook(typicalAddressBook);
        expectedBook.addApplication(AMY);
        assertEquals(expectedBook, new AddressBook(vbook));
    }

    @Test
    public void limit_maxStates_removesOldest() {
        VersionedAddressBook vbook = new VersionedAddressBook(new AddressBook());

        // commit continuously for 11 times (1 initial commit + 10 commits)
        for (int i = 0; i < 10; i++) {
            vbook.commit();
        }

        // Verify that the pointer is at the 10th bit (the index starts from 0)
        // Trying to undo ten times should be the limit
        for (int i = 0; i < 9; i++) {
            assertTrue(vbook.canUndo());
            vbook.undo();
        }

        // The 11th undo should fail because the earliest status has been removed from the list
        assertFalse(vbook.canUndo());
    }

    @Test
    public void undo_atStart_throwsException() {
        VersionedAddressBook vbook = new VersionedAddressBook(typicalAddressBook);
        assertThrows(VersionedAddressBook.NoUndoableStateException.class, vbook::undo);
    }

    @Test
    public void canRedo_afterUndo_returnsTrue() {
        VersionedAddressBook vbook = new VersionedAddressBook(typicalAddressBook);
        vbook.addApplication(AMY);
        vbook.commit();
        vbook.undo();

        assertTrue(vbook.canRedo());
    }

    @Test
    public void redo_afterUndo_restoresState() {
        VersionedAddressBook vbook = new VersionedAddressBook(typicalAddressBook);
        vbook.addApplication(AMY);
        vbook.commit();
        AddressBook expectedBook = new AddressBook(vbook);

        vbook.undo();
        vbook.redo();

        assertEquals(expectedBook, new AddressBook(vbook));
    }

    @Test
    public void commit_afterUndo_clearsRedoHistory() {
        VersionedAddressBook vbook = new VersionedAddressBook(typicalAddressBook);
        vbook.addApplication(AMY);
        vbook.commit();
        vbook.undo();

        vbook.commit();

        assertFalse(vbook.canRedo());
    }
}
