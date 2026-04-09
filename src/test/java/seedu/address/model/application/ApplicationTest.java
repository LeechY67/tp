package seedu.address.model.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_HREMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalApplications.BOB;
import static seedu.address.testutil.TypicalApplications.GOOGLE_SWE;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.ApplicationBuilder;

public class ApplicationTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Application application = new ApplicationBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> application.getTags().clear());
    }

    @Test
    public void isSameApplication() {
        // same object -> returns true
        assertTrue(GOOGLE_SWE.isSameApplication(GOOGLE_SWE));

        // null -> returns false
        assertFalse(GOOGLE_SWE.isSameApplication(null));

        // same role and company, all other attributes different -> returns true
        Application editedAlice = new ApplicationBuilder(GOOGLE_SWE)
                .withPhone(VALID_PHONE_BOB)
                .withHrEmail(VALID_HREMAIL_BOB)
                .withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(GOOGLE_SWE.isSameApplication(editedAlice));

        // same role and company name but different company location -> returns false
        editedAlice = new ApplicationBuilder(GOOGLE_SWE)
                .withCompanyLocation("Another Location")
                .build();
        assertFalse(GOOGLE_SWE.isSameApplication(editedAlice));

        // both locations empty -> returns true
        Application withEmptyLocation = new ApplicationBuilder(GOOGLE_SWE)
                .withCompanyLocation("")
                .build();
        Application bothEmptyLocation = new ApplicationBuilder(withEmptyLocation)
                .withPhone(VALID_PHONE_BOB)
                .build();
        assertTrue(withEmptyLocation.isSameApplication(bothEmptyLocation));

        // different role, all other attributes same -> returns false
        editedAlice = new ApplicationBuilder(GOOGLE_SWE).withRole(VALID_ROLE_BOB).build();
        assertFalse(GOOGLE_SWE.isSameApplication(editedAlice));

        // ------------------- 以下是针对模糊匹配的修改 -------------------

        // role differs in case -> 现在应该返回 true
        Application editedBob = new ApplicationBuilder(BOB).withRole(VALID_ROLE_BOB.toLowerCase()).build();
        assertTrue(BOB.isSameApplication(editedBob));

        // role has trailing spaces -> 现在应该返回 true
        String roleWithTrailingSpaces = VALID_ROLE_BOB + "  ";
        editedBob = new ApplicationBuilder(BOB).withRole(roleWithTrailingSpaces).build();
        assertTrue(BOB.isSameApplication(editedBob));

        // role has internal multiple spaces -> 现在应该返回 true
        String roleWithInternalSpaces = "Software    Engineer";
        Application app1 = new ApplicationBuilder().withRole("Software Engineer").build();
        Application app2 = new ApplicationBuilder().withRole(roleWithInternalSpaces).build();
        assertTrue(app1.isSameApplication(app2));

        // company name differs only in case, same location -> returns true
        Application caseChangedCompanyName = new ApplicationBuilder(GOOGLE_SWE)
                .withCompanyName(GOOGLE_SWE.getCompany().companyName.toUpperCase())
                .build();
        assertTrue(GOOGLE_SWE.isSameApplication(caseChangedCompanyName));

        // company name has internal multiple spaces -> returns true
        Application appWithSpacedCompany = new ApplicationBuilder(GOOGLE_SWE)
                .withCompanyName("Google    Inc")
                .build();
        Application appWithNormalCompany = new ApplicationBuilder(GOOGLE_SWE)
                .withCompanyName("Google Inc")
                .build();
        assertTrue(appWithSpacedCompany.isSameApplication(appWithNormalCompany));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Application aliceCopy = new ApplicationBuilder(GOOGLE_SWE).build();
        assertTrue(GOOGLE_SWE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(GOOGLE_SWE.equals(GOOGLE_SWE));

        // different type -> returns false
        assertFalse(GOOGLE_SWE.equals(5));

        // different application -> returns false
        assertFalse(GOOGLE_SWE.equals(BOB));

        // different role -> returns false
        Application editedAlice = new ApplicationBuilder(GOOGLE_SWE).withRole(VALID_ROLE_BOB).build();
        assertFalse(GOOGLE_SWE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new ApplicationBuilder(GOOGLE_SWE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(GOOGLE_SWE.equals(editedAlice));

        // different hrEmail -> returns false
        editedAlice = new ApplicationBuilder(GOOGLE_SWE).withHrEmail(VALID_HREMAIL_BOB).build();
        assertFalse(GOOGLE_SWE.equals(editedAlice));

        // different company -> returns false
        editedAlice = new ApplicationBuilder(GOOGLE_SWE).withCompany("Some Other Company").build();
        assertFalse(GOOGLE_SWE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new ApplicationBuilder(GOOGLE_SWE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(GOOGLE_SWE.equals(editedAlice));
    }

    @Test
    public void equals_sameValuesDifferentStatus_returnsFalse() {
        Application editedAlice = new ApplicationBuilder(GOOGLE_SWE)
                .withStatus(Status.REJECTED)
                .build();
        assertFalse(GOOGLE_SWE.equals(editedAlice));
    }

    @Test
    public void constructor_withoutStatus_defaultsToApplied() {
        Application applicationWithDefaultStatus = new Application(
                GOOGLE_SWE.getRole(),
                GOOGLE_SWE.getPhone(),
                GOOGLE_SWE.getHrEmail(),
                GOOGLE_SWE.getCompany(),
                GOOGLE_SWE.getTags());

        assertEquals(Status.APPLIED, applicationWithDefaultStatus.getStatus());
        assertEquals(Deadline.getEmptyDeadline(), applicationWithDefaultStatus.getDeadline());
        assertEquals(new Note(""), applicationWithDefaultStatus.getNote());
        assertEquals(Resume.getEmptyResume(), applicationWithDefaultStatus.getResume());
        assertFalse(applicationWithDefaultStatus.hasResume());
    }

    @Test
    public void toStringMethod() {
        String expected = Application.class.getCanonicalName()
                + "{role=" + GOOGLE_SWE.getRole()
                + ", phone=" + GOOGLE_SWE.getPhone()
                + ", hrEmail=" + GOOGLE_SWE.getHrEmail()
                + ", company=" + GOOGLE_SWE.getCompany()
                + ", tags=" + GOOGLE_SWE.getTags()
                + ", status=" + GOOGLE_SWE.getStatus()
                + ", deadline=" + GOOGLE_SWE.getDeadline()
                + ", applicationEvent=" + GOOGLE_SWE.getApplicationEvent()
                + ", note=" + GOOGLE_SWE.getNote()
                + ", resume=" + GOOGLE_SWE.getResume() + "}";
        assertEquals(expected, GOOGLE_SWE.toString());
    }

    @Test
    public void hashCode_sameValues_sameHash() {
        Application copyOfAlice = new ApplicationBuilder(GOOGLE_SWE).build();
        assertEquals(GOOGLE_SWE.hashCode(), copyOfAlice.hashCode());
    }
}
