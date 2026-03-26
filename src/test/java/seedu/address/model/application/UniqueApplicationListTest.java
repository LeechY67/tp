package seedu.address.model.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalApplications.BOB;
import static seedu.address.testutil.TypicalApplications.GOOGLE_SWE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.application.exceptions.ApplicationNotFoundException;
import seedu.address.model.application.exceptions.DuplicateApplicationException;
import seedu.address.testutil.ApplicationBuilder;

public class UniqueApplicationListTest {

    private final UniqueApplicationList uniqueApplicationList = new UniqueApplicationList();

    @Test
    public void contains_nullApplication_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueApplicationList.contains(null));
    }

    @Test
    public void contains_applicationNotInList_returnsFalse() {
        assertFalse(uniqueApplicationList.contains(GOOGLE_SWE));
    }

    @Test
    public void contains_applicationInList_returnsTrue() {
        uniqueApplicationList.add(GOOGLE_SWE);
        assertTrue(uniqueApplicationList.contains(GOOGLE_SWE));
    }

    @Test
    public void contains_applicationWithSameIdentityFieldsInList_returnsTrue() {
        uniqueApplicationList.add(GOOGLE_SWE);
        Application editedAlice = new ApplicationBuilder(GOOGLE_SWE)
                .withPhone("99999999")
                .withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(uniqueApplicationList.contains(editedAlice));
    }

    @Test
    public void add_nullApplication_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueApplicationList.add(null));
    }

    @Test
    public void add_duplicateApplication_throwsDuplicateApplicationException() {
        uniqueApplicationList.add(GOOGLE_SWE);
        assertThrows(DuplicateApplicationException.class, () -> uniqueApplicationList.add(GOOGLE_SWE));
    }

    @Test
    public void setApplication_nullTargetApplication_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueApplicationList.setApplication(null, GOOGLE_SWE));
    }

    @Test
    public void setApplication_nullEditedApplication_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueApplicationList.setApplication(GOOGLE_SWE, null));
    }

    @Test
    public void setApplication_targetApplicationNotInList_throwsApplicationNotFoundException() {
        assertThrows(ApplicationNotFoundException.class, () -> uniqueApplicationList
                .setApplication(GOOGLE_SWE, GOOGLE_SWE));
    }

    @Test
    public void setApplication_editedApplicationIsSameApplication_success() {
        uniqueApplicationList.add(GOOGLE_SWE);
        uniqueApplicationList.setApplication(GOOGLE_SWE, GOOGLE_SWE);
        UniqueApplicationList expectedUniqueApplicationList = new UniqueApplicationList();
        expectedUniqueApplicationList.add(GOOGLE_SWE);
        assertEquals(expectedUniqueApplicationList, uniqueApplicationList);
    }

    @Test
    public void setApplication_editedApplicationHasSameIdentity_success() {
        uniqueApplicationList.add(GOOGLE_SWE);
        Application editedAlice = new ApplicationBuilder(GOOGLE_SWE)
                .withTags(VALID_TAG_HUSBAND)
                .withTags(VALID_TAG_HUSBAND)
                .build();
        uniqueApplicationList.setApplication(GOOGLE_SWE, editedAlice);
        UniqueApplicationList expectedUniqueApplicationList = new UniqueApplicationList();
        expectedUniqueApplicationList.add(editedAlice);
        assertEquals(expectedUniqueApplicationList, uniqueApplicationList);
    }

    @Test
    public void setApplication_editedApplicationHasDifferentIdentity_success() {
        uniqueApplicationList.add(GOOGLE_SWE);
        uniqueApplicationList.setApplication(GOOGLE_SWE, BOB);
        UniqueApplicationList expectedUniqueApplicationList = new UniqueApplicationList();
        expectedUniqueApplicationList.add(BOB);
        assertEquals(expectedUniqueApplicationList, uniqueApplicationList);
    }

    @Test
    public void setApplication_editedApplicationHasNonUniqueIdentity_throwsDuplicateApplicationException() {
        uniqueApplicationList.add(GOOGLE_SWE);
        uniqueApplicationList.add(BOB);
        assertThrows(DuplicateApplicationException.class, () -> uniqueApplicationList.setApplication(GOOGLE_SWE, BOB));
    }

    @Test
    public void remove_nullApplication_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueApplicationList.remove(null));
    }

    @Test
    public void remove_applicationDoesNotExist_throwsApplicationNotFoundException() {
        assertThrows(ApplicationNotFoundException.class, () -> uniqueApplicationList.remove(GOOGLE_SWE));
    }

    @Test
    public void remove_existingApplication_removesApplication() {
        uniqueApplicationList.add(GOOGLE_SWE);
        uniqueApplicationList.remove(GOOGLE_SWE);
        UniqueApplicationList expectedUniqueApplicationList = new UniqueApplicationList();
        assertEquals(expectedUniqueApplicationList, uniqueApplicationList);
    }

    @Test
    public void setApplications_nullUniqueApplicationList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                uniqueApplicationList.setApplications((UniqueApplicationList) null));
    }

    @Test
    public void setApplications_uniqueApplicationList_replacesOwnListWithProvidedUniqueApplicationList() {
        uniqueApplicationList.add(GOOGLE_SWE);
        UniqueApplicationList expectedUniqueApplicationList = new UniqueApplicationList();
        expectedUniqueApplicationList.add(BOB);
        uniqueApplicationList.setApplications(expectedUniqueApplicationList);
        assertEquals(expectedUniqueApplicationList, uniqueApplicationList);
    }

    @Test
    public void setApplications_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueApplicationList.setApplications((List<Application>) null));
    }

    @Test
    public void setApplications_list_replacesOwnListWithProvidedList() {
        uniqueApplicationList.add(GOOGLE_SWE);
        List<Application> applicationList = Collections.singletonList(BOB);
        uniqueApplicationList.setApplications(applicationList);
        UniqueApplicationList expectedUniqueApplicationList = new UniqueApplicationList();
        expectedUniqueApplicationList.add(BOB);
        assertEquals(expectedUniqueApplicationList, uniqueApplicationList);
    }

    @Test
    public void setApplications_listWithDuplicateApplications_throwsDuplicateApplicationException() {
        List<Application> listWithDuplicateApplications = Arrays.asList(GOOGLE_SWE, GOOGLE_SWE);
        assertThrows(DuplicateApplicationException.class, () ->
                uniqueApplicationList.setApplications(listWithDuplicateApplications));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () ->
                uniqueApplicationList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueApplicationList.asUnmodifiableObservableList().toString(), uniqueApplicationList.toString());
    }
}
