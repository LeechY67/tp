package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalApplications.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_APPLICATION;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_APPLICATION;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.application.ApplicationEvent;
import seedu.address.model.application.Interview;

public class InterviewCommandTest {

    private static final LocalDateTime VALID_DATETIME = LocalDateTime.of(2026, 12, 31, 23, 59);

    private final ModelManager model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    // ── execute: success ──────────────────────────────────────────────────────

    @Test
    public void execute_allFieldsSpecified_success() throws Exception {
        Interview interview = new Interview("Google HQ", VALID_DATETIME, "John Doe", "technical");
        InterviewCommand command = new InterviewCommand(INDEX_FIRST_APPLICATION, interview);

        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("Interview updated for:"));
    }

    @Test
    public void execute_optionalFieldsOmitted_success() throws Exception {
        Interview interview = new Interview("Zoom", VALID_DATETIME);
        InterviewCommand command = new InterviewCommand(INDEX_FIRST_APPLICATION, interview);

        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("Interview updated for:"));
    }

    @Test
    public void execute_onlyInterviewerProvided_success() throws Exception {
        Interview interview = new Interview("Office", VALID_DATETIME, "Jane Smith", null);
        InterviewCommand command = new InterviewCommand(INDEX_FIRST_APPLICATION, interview);

        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("Interview updated for:"));
    }

    @Test
    public void execute_onlyInterviewTypeProvided_success() throws Exception {
        Interview interview = new Interview("Office", VALID_DATETIME, null, "behavioural");
        InterviewCommand command = new InterviewCommand(INDEX_FIRST_APPLICATION, interview);

        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("Interview updated for:"));
    }

    @Test
    public void execute_secondIndex_success() throws Exception {
        Interview interview = new Interview("Remote", VALID_DATETIME, "Alice", "panel");
        InterviewCommand command = new InterviewCommand(INDEX_SECOND_APPLICATION, interview);

        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("Interview updated for:"));
    }

    // ── execute: invalid index ────────────────────────────────────────────────

    @Test
    public void execute_outOfBoundsIndex_throwsCommandException() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredApplicationList().size() + 1);
        Interview interview = new Interview("Office", VALID_DATETIME, "John", "technical");
        InterviewCommand command = new InterviewCommand(outOfBoundsIndex, interview);

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    // ── equals ────────────────────────────────────────────────────────────────

    @Test
    public void equals_sameObject_returnsTrue() {
        Interview interview = new Interview("Google HQ", VALID_DATETIME, "John", "technical");
        InterviewCommand command = new InterviewCommand(INDEX_FIRST_APPLICATION, interview);
        assertEquals(command, command);
    }

    @Test
    public void equals_sameFields_returnsTrue() {
        Interview interview = new Interview("Google HQ", VALID_DATETIME, "John", "technical");
        InterviewCommand a = new InterviewCommand(INDEX_FIRST_APPLICATION, interview);
        InterviewCommand b = new InterviewCommand(INDEX_FIRST_APPLICATION, interview);
        assertEquals(a, b);
    }

    @Test
    public void equals_differentIndex_returnsFalse() {
        Interview interview = new Interview("Google HQ", VALID_DATETIME, "John", "technical");
        InterviewCommand a = new InterviewCommand(INDEX_FIRST_APPLICATION, interview);
        InterviewCommand b = new InterviewCommand(INDEX_SECOND_APPLICATION, interview);
        assertFalse(a.equals(b));
    }

    @Test
    public void equals_differentEvent_returnsFalse() {
        Interview interview1 = new Interview("Google HQ", VALID_DATETIME, "John", "technical");
        Interview interview2 = new Interview("Zoom", VALID_DATETIME, "Jane", "behavioural");
        InterviewCommand a = new InterviewCommand(INDEX_FIRST_APPLICATION, interview1);
        InterviewCommand b = new InterviewCommand(INDEX_FIRST_APPLICATION, interview2);
        assertFalse(a.equals(b));
    }

    @Test
    public void equals_nullObject_returnsFalse() {
        Interview interview = new Interview("Google HQ", VALID_DATETIME, "John", "technical");
        InterviewCommand command = new InterviewCommand(INDEX_FIRST_APPLICATION, interview);
        assertFalse(command.equals(null));
    }

    @Test
    public void equals_differentCommandType_returnsFalse() {
        Interview interview = new Interview("Google HQ", VALID_DATETIME, "John", "technical");
        InterviewCommand interviewCommand = new InterviewCommand(INDEX_FIRST_APPLICATION, interview);
        ApplicationEvent oa = new seedu.address.model.application.OnlineAssessment(
                "home", VALID_DATETIME, "HackerRank", "www.hr.com");
        AssessmentCommand assessmentCommand = new AssessmentCommand(INDEX_FIRST_APPLICATION, oa);
        assertFalse(interviewCommand.equals(assessmentCommand));
    }
}
