package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_DATETIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSESSMENT_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSESSMENT_PLATFORM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_TIME;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AssessmentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.application.ApplicationEvent;
import seedu.address.model.application.OnlineAssessment;


/**
 * Parse the input parameters and create a new DeadlineCommand objects
 */
public class AssessmentCommandParser implements Parser<AssessmentCommand> {

    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Parse the given {@code String} parameter.
     *
     * @throws ParseException If the user input does not conform to the expected format
     */
    public AssessmentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_EVENT_LOCATION, PREFIX_EVENT_TIME, PREFIX_ASSESSMENT_PLATFORM,
                        PREFIX_ASSESSMENT_LINK);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AssessmentCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_EVENT_LOCATION, PREFIX_EVENT_TIME, PREFIX_ASSESSMENT_PLATFORM,
                PREFIX_ASSESSMENT_LINK);
        if (!argMultimap.getValue(PREFIX_EVENT_LOCATION).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssessmentCommand.MESSAGE_USAGE));
        }

        if (!argMultimap.getValue(PREFIX_EVENT_TIME).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssessmentCommand.MESSAGE_USAGE));
        }

        LocalDateTime dateTime = parseLocalDateTime(argMultimap.getValue(PREFIX_EVENT_TIME).get());

        if (!argMultimap.getValue(PREFIX_ASSESSMENT_PLATFORM).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssessmentCommand.MESSAGE_USAGE));
        }

        if (!argMultimap.getValue(PREFIX_ASSESSMENT_LINK).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssessmentCommand.MESSAGE_USAGE));
        }

        ApplicationEvent applicationEvent = new OnlineAssessment(argMultimap.getValue(PREFIX_EVENT_LOCATION).get(),
                dateTime,
                argMultimap.getValue(PREFIX_ASSESSMENT_PLATFORM).get(),
                argMultimap.getValue(PREFIX_ASSESSMENT_LINK).get());
        return new AssessmentCommand(index, applicationEvent);


    }

    /**
     * parse String to local date time
     *
     * @param args
     * @return LocalDateTime
     * @throws DateTimeParseException
     * @throws ParseException
     */
    public static LocalDateTime parseLocalDateTime(String args) throws DateTimeParseException, ParseException {
        LocalDateTime dateTime = null;
        try {
            dateTime = LocalDateTime.parse(args, DATETIME_FORMATTER);
        } catch (DateTimeParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_DATETIME, AssessmentCommand.DATETIME_USAGE));
        }
        return dateTime;
    }
}
