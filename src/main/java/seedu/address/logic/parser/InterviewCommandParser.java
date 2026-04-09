package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_DATETIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERVIEW_INTERVIEWER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INTERVIEW_TYPE;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.InterviewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.application.ApplicationEvent;
import seedu.address.model.application.Interview;

/**
 * Parses input arguments and creates a new {@code InterviewCommand} object.
 * <p>
 * Required prefixes: {@code el/} (event location), {@code et/} (event time).
 * Optional prefixes: {@code in/} (interviewer name), {@code it/} (interview type).
 */
public class InterviewCommandParser implements Parser<InterviewCommand> {

    public static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Parses the given {@code String} of arguments and returns an {@code InterviewCommand}.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public InterviewCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args,
                PREFIX_EVENT_LOCATION,
                PREFIX_EVENT_TIME,
                PREFIX_INTERVIEW_INTERVIEWER,
                PREFIX_INTERVIEW_TYPE);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, InterviewCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_EVENT_LOCATION,
                PREFIX_EVENT_TIME,
                PREFIX_INTERVIEW_INTERVIEWER,
                PREFIX_INTERVIEW_TYPE);

        // location is required
        if (!argMultimap.getValue(PREFIX_EVENT_LOCATION).isPresent()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, InterviewCommand.MESSAGE_USAGE));
        }

        // time is required
        if (!argMultimap.getValue(PREFIX_EVENT_TIME).isPresent()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, InterviewCommand.MESSAGE_USAGE));
        }

        LocalDateTime dateTime = parseLocalDateTime(argMultimap.getValue(PREFIX_EVENT_TIME).get());

        String location = argMultimap.getValue(PREFIX_EVENT_LOCATION).get();

        // interviewer name and interview type are optional
        String interviewerName = argMultimap.getValue(PREFIX_INTERVIEW_INTERVIEWER).orElse(null);
        String interviewType = argMultimap.getValue(PREFIX_INTERVIEW_TYPE).orElse(null);

        ApplicationEvent applicationEvent = new Interview(location, dateTime, interviewerName, interviewType);
        return new InterviewCommand(index, applicationEvent);
    }

    /**
     * Parses a datetime string into a {@code LocalDateTime}.
     *
     * @throws ParseException if the string does not match the expected format
     */
    public static LocalDateTime parseLocalDateTime(String args) throws ParseException {
        try {
            return LocalDateTime.parse(args, DATETIME_FORMATTER);
        } catch (DateTimeParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_DATETIME, InterviewCommand.DATETIME_USAGE));
        }
    }
}
