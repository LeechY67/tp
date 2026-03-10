package seedu.address.model.application;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AddressTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Company(null));
    }

    @Test
    public void constructor_invalidAddress_throwsIllegalArgumentException() {
        String invalidAddress = "";
        assertThrows(IllegalArgumentException.class, () -> new Company(invalidAddress));
    }

    @Test
    public void isValidAddress() {
        // null address
        assertThrows(NullPointerException.class, () -> Company.isValidAddress(null));

        // invalid addresses
        assertFalse(Company.isValidAddress("")); // empty string
        assertFalse(Company.isValidAddress(" ")); // spaces only

        // valid addresses
        assertTrue(Company.isValidAddress("Blk 456, Den Road, #01-355"));
        assertTrue(Company.isValidAddress("-")); // one character
        assertTrue(Company.isValidAddress("Leng Inc; 1234 Market St; San Francisco CA 2349879; USA")); // long address
    }

    @Test
    public void equals() {
        Company company = new Company("Valid Address");

        // same values -> returns true
        assertTrue(company.equals(new Company("Valid Address")));

        // same object -> returns true
        assertTrue(company.equals(company));

        // null -> returns false
        assertFalse(company.equals(null));

        // different types -> returns false
        assertFalse(company.equals(5.0f));

        // different values -> returns false
        assertFalse(company.equals(new Company("Other Valid Address")));
    }
}
