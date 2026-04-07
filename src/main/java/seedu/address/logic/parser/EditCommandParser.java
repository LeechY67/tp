package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMPANY_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMPANY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HREMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditApplicationDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.application.Status;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    private static final Set<String> VALID_PREFIXES = Set.of(
            PREFIX_ROLE.getPrefix(),
            PREFIX_PHONE.getPrefix(),
            PREFIX_HREMAIL.getPrefix(),
            PREFIX_COMPANY_NAME.getPrefix(),
            PREFIX_COMPANY_LOCATION.getPrefix(),
            PREFIX_TAG.getPrefix(),
            PREFIX_STATUS.getPrefix(),
            PREFIX_DEADLINE.getPrefix(),
            PREFIX_NOTE.getPrefix()
    );

    private static final Pattern PREFIX_LIKE_PATTERN = Pattern.compile("(^|\\s)([A-Za-z]+/)");

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        checkForInvalidPrefixes(args, VALID_PREFIXES);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ROLE, PREFIX_PHONE, PREFIX_HREMAIL,
                        PREFIX_COMPANY_NAME, PREFIX_COMPANY_LOCATION, PREFIX_TAG,
                        PREFIX_STATUS, PREFIX_DEADLINE, PREFIX_NOTE);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ROLE, PREFIX_PHONE, PREFIX_HREMAIL,
                PREFIX_COMPANY_NAME, PREFIX_COMPANY_LOCATION, PREFIX_DEADLINE, PREFIX_NOTE);

        EditApplicationDescriptor editApplicationDescriptor = new EditApplicationDescriptor();

        if (argMultimap.getValue(PREFIX_ROLE).isPresent()) {
            editApplicationDescriptor.setRole(ParserUtil.parseName(argMultimap.getValue(PREFIX_ROLE).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editApplicationDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_HREMAIL).isPresent()) {
            editApplicationDescriptor.setHrEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_HREMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_COMPANY_NAME).isPresent()) {
            editApplicationDescriptor.setCompanyName(
                    ParserUtil.parseCompanyName(argMultimap.getValue(PREFIX_COMPANY_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_COMPANY_LOCATION).isPresent()) {
            editApplicationDescriptor.setCompanyLocation(
                    ParserUtil.parseCompanyLocation(argMultimap.getValue(PREFIX_COMPANY_LOCATION).get()));
        }
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editApplicationDescriptor::setTags);

        if (argMultimap.getValue(PREFIX_STATUS).isPresent()) {
            try {
                Status status = Status.valueOf(argMultimap.getValue(PREFIX_STATUS).get().toUpperCase());
                editApplicationDescriptor.setStatus(status);
            } catch (IllegalArgumentException e) {
                throw new ParseException("Invalid status. Valid values: APPLIED,"
                        + " INTERVIEWING, OFFERED, REJECTED, WITHDRAWN");
            }
        }

        if (argMultimap.getValue(PREFIX_DEADLINE).isPresent()) {
            editApplicationDescriptor.setDeadline(ParserUtil.parseDeadline(argMultimap.getValue(PREFIX_DEADLINE)
                    .get()));
        }

        if (argMultimap.getValue(PREFIX_NOTE).isPresent()) {
            editApplicationDescriptor.setNote(
                    ParserUtil.parseNote(argMultimap.getValue(PREFIX_NOTE).get()));
        }

        if (!editApplicationDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editApplicationDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

    /**
     * Rejects any prefix-like token that is not in the allowed prefix set for this command.
     */
    private static void checkForInvalidPrefixes(String args, Set<String> validPrefixes) throws ParseException {
        Matcher matcher = PREFIX_LIKE_PATTERN.matcher(args);

        while (matcher.find()) {
            String foundPrefix = matcher.group(2);
            if (!validPrefixes.contains(foundPrefix)) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
            }
        }
    }
}
