package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.application.Company;
import seedu.address.model.application.Deadline;
import seedu.address.model.application.HrEmail;
import seedu.address.model.application.Note;
import seedu.address.model.application.Phone;
import seedu.address.model.application.Resume;
import seedu.address.model.application.Role;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String resume} into a {@code Resume}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code resume} is invalid.
     */
    public static Resume parseResume(String resume) throws ParseException {
        requireNonNull(resume);
        String trimmedResume = resume.trim();
        if (!Resume.isValidResume(trimmedResume)) {
            throw new ParseException(Resume.MESSAGE_CONSTRAINTS);
        }
        return new Resume(trimmedResume);
    }

    /**
     * Parses a {@code String role} into a {@code Role}.
     * Normalizes the input by trimming whitespaces and compressing multiple internal spaces into a single space.
     * @throws ParseException if the given {@code role} is invalid.
     */
    public static Role parseRole(String role) throws ParseException {
        requireNonNull(role);
        String normalizedRole = role.trim().replaceAll("\\s+", " ");
        if (!Role.isValidRole(normalizedRole)) {
            throw new ParseException(Role.MESSAGE_CONSTRAINTS);
        }
        return new Role(normalizedRole);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String companyName} and returns a sanitized string.
     * Normalizes internal whitespaces to ensure consistent duplicate detection and UI display.
     * @throws ParseException if the given {@code companyName} is invalid.
     */
    public static String parseCompanyName(String companyName) throws ParseException {
        requireNonNull(companyName);
        String normalizedName = companyName.trim().replaceAll("\\s+", " ");
        if (!Company.isValidCompanyName(normalizedName)) {
            throw new ParseException(Company.MESSAGE_CONSTRAINTS_NAME);
        }
        return normalizedName;
    }

    /**
     * Parses a {@code String location} and returns a sanitized string.
     * Normalizes whitespaces to maintain data integrity across different input formats.
     */
    public static String parseCompanyLocation(String location) throws ParseException {
        requireNonNull(location);
        String normalizedLocation = location.trim().replaceAll("\\s+", " ");
        if (!Company.isValidCompanyLocation(normalizedLocation)) {
            throw new ParseException(Company.MESSAGE_CONSTRAINTS_LOCATION);
        }
        return normalizedLocation;
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static HrEmail parseHrEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!HrEmail.isValidHrEmail(trimmedEmail)) {
            throw new ParseException(HrEmail.MESSAGE_CONSTRAINTS);
        }
        return new HrEmail(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String deadline} into a {@code Deadline}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the format or the date value is invalid.
     */
    public static Deadline parseDeadline(String deadline) throws ParseException {
        if (deadline == null || deadline.isBlank()) {
            return Deadline.getEmptyDeadline();
        }

        String trimmedDeadline = deadline.trim();

        if (trimmedDeadline.equals(Deadline.PLACEHOLDER_DEADLINE)) {
            return Deadline.getEmptyDeadline();
        }

        if (!Deadline.isValidFormat(trimmedDeadline)) {
            throw new ParseException(Deadline.MESSAGE_CONSTRAINTS_FORMAT);
        }

        if (!Deadline.isValidCalendarDate(trimmedDeadline)) {
            throw new ParseException(Deadline.MESSAGE_CONSTRAINTS_DATE);
        }

        return new Deadline(trimmedDeadline);
    }

    /**
     * Parses a {@code String note} into a {@code Note}.
     * Normalizes the note content by trimming and compressing redundant internal whitespaces.
     * @throws ParseException if the given {@code note} fails length constraints.
     */
    public static Note parseNote(String note) throws ParseException {
        requireNonNull(note);
        String normalizedNote = note.trim().replaceAll("\\s+", " ");
        if (!Note.isValidNote(normalizedNote)) {
            throw new ParseException(Note.MESSAGE_CONSTRAINTS);
        }
        return new Note(normalizedNote);
    }
}
