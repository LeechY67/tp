package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_DATETIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERVIEW_INTERVIEWER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERVIEW_TYPE;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_APPLICATION;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.InterviewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.application.Interview;

public class InterviewCommandParserTest {

    private static final String VALID_DATETIME_STRING = "2026-12-31 23:59";
    private static final LocalDateTime VALID_DATETIME = LocalDateTime.of(2026, 12, 31, 23, 59);

    private final InterviewCommandParser parser = new InterviewCommandParser();

    // ── success: all fields ───────────────────────────────────────────────────

    @Test
    public void parse_allFieldsPresent_success() throws Exception {
        InterviewCommand command = parser.parse(" 1 "
                + PREFIX_EVENT_LOCATION + "Google HQ "
                + PREFIX_EVENT_TIME + VALID_DATETIME_STRING + " "
                + PREFIX_INTERVIEW_INTERVIEWER + "John Doe "
                + PREFIX_INTERVIEW_TYPE + "technical");

        InterviewCommand expected = new InterviewCommand(INDEX_FIRST_APPLICATION,
                new Interview("Google HQ", VALID_DATETIME, "John Doe", "technical"));

        assertEquals(expected, command);
    }

    // ── success: optional fields omitted ─────────────────────────────────────

    @Test
    public void parse_optionalFieldsOmitted_success() throws Exception {
        InterviewCommand command = parser.parse(" 1 "
                + PREFIX_EVENT_LOCATION + "Zoom "
                + PREFIX_EVENT_TIME + VALID_DATETIME_STRING);

        InterviewCommand expected = new InterviewCommand(INDEX_FIRST_APPLICATION,
                new Interview("Zoom", VALID_DATETIME, null, null));

        assertEquals(expected, command);
    }

    @Test
    public void parse_onlyInterviewerProvided_success() throws Exception {
        InterviewCommand command = parser.parse(" 1 "
                + PREFIX_EVENT_LOCATION + "Office "
                + PREFIX_EVENT_TIME + VALID_DATETIME_STRING + " "
                + PREFIX_INTERVIEW_INTERVIEWER + "Jane");

        InterviewCommand expected = new InterviewCommand(INDEX_FIRST_APPLICATION,
                new Interview("Office", VALID_DATETIME, "Jane", null));

        assertEquals(expected, command);
    }

    @Test
    public void parse_onlyInterviewTypeProvided_success() throws Exception {
        InterviewCommand command = parser.parse(" 1 "
                + PREFIX_EVENT_LOCATION + "Remote "
                + PREFIX_EVENT_TIME + VALID_DATETIME_STRING + " "
                + PREFIX_INTERVIEW_TYPE + "behavioural");

        InterviewCommand expected = new InterviewCommand(INDEX_FIRST_APPLICATION,
                new Interview("Remote", VALID_DATETIME, null, "behavioural"));

        assertEquals(expected, command);
    }

    @Test
    public void parse_extraWhitespace_success() throws Exception {
        InterviewCommand command = parser.parse("   1   "
                + PREFIX_EVENT_LOCATION + "Google HQ "
                + PREFIX_EVENT_TIME + VALID_DATETIME_STRING + " "
                + PREFIX_INTERVIEW_INTERVIEWER + "John Doe "
                + PREFIX_INTERVIEW_TYPE + "technical");

        InterviewCommand expected = new InterviewCommand(INDEX_FIRST_APPLICATION,
                new Interview("Google HQ", VALID_DATETIME, "John Doe", "technical"));

        assertEquals(expected, command);
    }

    // ── failure: missing required fields ─────────────────────────────────────

    @Test
    public void parse_missingLocation_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, InterviewCommand.MESSAGE_USAGE), ()
                        -> parser.parse(" 1 "
                        + PREFIX_EVENT_TIME + VALID_DATETIME_STRING + " "
                        + PREFIX_INTERVIEW_INTERVIEWER + "John "
                        + PREFIX_INTERVIEW_TYPE + "technical"));
    }

    @Test
    public void parse_missingEventTime_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, InterviewCommand.MESSAGE_USAGE), ()
                        -> parser.parse(" 1 "
                        + PREFIX_EVENT_LOCATION + "Google HQ "
                        + PREFIX_INTERVIEW_INTERVIEWER + "John "
                        + PREFIX_INTERVIEW_TYPE + "technical"));
    }

    @Test
    public void parse_missingIndex_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, InterviewCommand.MESSAGE_USAGE), ()
                        -> parser.parse(PREFIX_EVENT_LOCATION + "Google HQ "
                        + PREFIX_EVENT_TIME + VALID_DATETIME_STRING + " "
                        + PREFIX_INTERVIEW_INTERVIEWER + "John "
                        + PREFIX_INTERVIEW_TYPE + "technical"));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, InterviewCommand.MESSAGE_USAGE), ()
                        -> parser.parse(""));
    }

    // ── failure: invalid index ────────────────────────────────────────────────

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertThrows(ParseException.class, ()
                -> parser.parse(" abc "
                + PREFIX_EVENT_LOCATION + "Office "
                + PREFIX_EVENT_TIME + VALID_DATETIME_STRING + " "
                + PREFIX_INTERVIEW_INTERVIEWER + "John "
                + PREFIX_INTERVIEW_TYPE + "technical"));
    }

    @Test
    public void parse_zeroIndex_throwsParseException() {
        assertThrows(ParseException.class, ()
                -> parser.parse(" 0 "
                + PREFIX_EVENT_LOCATION + "Office "
                + PREFIX_EVENT_TIME + VALID_DATETIME_STRING + " "
                + PREFIX_INTERVIEW_INTERVIEWER + "John "
                + PREFIX_INTERVIEW_TYPE + "technical"));
    }

    @Test
    public void parse_negativeIndex_throwsParseException() {
        assertThrows(ParseException.class, ()
                -> parser.parse(" -1 "
                + PREFIX_EVENT_LOCATION + "Office "
                + PREFIX_EVENT_TIME + VALID_DATETIME_STRING + " "
                + PREFIX_INTERVIEW_INTERVIEWER + "John "
                + PREFIX_INTERVIEW_TYPE + "technical"));
    }

    // ── failure: invalid datetime ─────────────────────────────────────────────

    @Test
    public void parse_invalidDateTimeFormat_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_DATETIME, InterviewCommand.DATETIME_USAGE), ()
                        -> parser.parse(" 1 "
                        + PREFIX_EVENT_LOCATION + "Google HQ "
                        + PREFIX_EVENT_TIME + "31-12-2026 23:59 " // wrong format
                        + PREFIX_INTERVIEW_INTERVIEWER + "John "
                        + PREFIX_INTERVIEW_TYPE + "technical"));
    }

    @Test
    public void parse_invalidDateTimeText_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_DATETIME, InterviewCommand.DATETIME_USAGE), ()
                        -> parser.parse(" 1 "
                        + PREFIX_EVENT_LOCATION + "Office "
                        + PREFIX_EVENT_TIME + "not-a-date "));
    }

    // ── failure: duplicate prefixes ───────────────────────────────────────────

    @Test
    public void parse_duplicateLocation_throwsParseException() {
        assertThrows(ParseException.class, ()
                -> parser.parse(" 1 "
                + PREFIX_EVENT_LOCATION + "Google HQ "
                + PREFIX_EVENT_LOCATION + "Zoom "
                + PREFIX_EVENT_TIME + VALID_DATETIME_STRING + " "
                + PREFIX_INTERVIEW_INTERVIEWER + "John "
                + PREFIX_INTERVIEW_TYPE + "technical"));
    }

    @Test
    public void parse_duplicateInterviewer_throwsParseException() {
        assertThrows(ParseException.class, ()
                -> parser.parse(" 1 "
                + PREFIX_EVENT_LOCATION + "Office "
                + PREFIX_EVENT_TIME + VALID_DATETIME_STRING + " "
                + PREFIX_INTERVIEW_INTERVIEWER + "John "
                + PREFIX_INTERVIEW_INTERVIEWER + "Jane "
                + PREFIX_INTERVIEW_TYPE + "technical"));
    }

    @Test
    public void parse_duplicateInterviewType_throwsParseException() {
        assertThrows(ParseException.class, ()
                -> parser.parse(" 1 "
                + PREFIX_EVENT_LOCATION + "Office "
                + PREFIX_EVENT_TIME + VALID_DATETIME_STRING + " "
                + PREFIX_INTERVIEW_INTERVIEWER + "John "
                + PREFIX_INTERVIEW_TYPE + "technical "
                + PREFIX_INTERVIEW_TYPE + "behavioural"));
    }
}
