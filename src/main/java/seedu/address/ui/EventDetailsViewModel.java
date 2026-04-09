package seedu.address.ui;

import java.time.format.DateTimeFormatter;

import seedu.address.model.application.ApplicationEvent;
import seedu.address.model.application.Interview;
import seedu.address.model.application.OnlineAssessment;

/**
 * Pure-Java view model for {@link EventDetailsWindow}.
 *
 * <p>Encapsulates all branching logic that decides what text to display for
 * each label, with no dependency on JavaFX. This makes the logic fully
 * unit-testable without a running FX toolkit.
 *
 * <p>Fields that are not relevant to the current event type are set to
 * {@code null}; the window uses {@code null} to hide the corresponding rows.
 */
public class EventDetailsViewModel {

    static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

    private final String title;
    private final String location;
    private final String dateTime;

    // OnlineAssessment fields (null when not applicable)
    private final String platform;
    private final String link;

    // Interview fields (null when not applicable)
    private final String interviewerName;
    private final String interviewType;

    /**
     * Builds a view model from the given {@code ApplicationEvent}.
     *
     * @param event the event whose details are to be displayed; must not be null.
     */
    public EventDetailsViewModel(ApplicationEvent event) {
        this.location = event.getLocation();
        this.dateTime = event.getLocalDate().format(DISPLAY_FORMATTER);

        if (event instanceof OnlineAssessment oa) {
            this.title = "Online Assessment Details";
            this.platform = oa.getPlatform();
            this.link = oa.getLink();
            this.interviewerName = null;
            this.interviewType = null;

        } else if (event instanceof Interview iv) {
            this.title = "Interview Details";
            this.platform = null;
            this.link = null;
            // Treat empty optional fields as null so the window can hide them
            String name = iv.getInterviewerName();
            this.interviewerName = (name != null && !name.isBlank()) ? name : null;
            String type = iv.getInterviewType();
            this.interviewType = (type != null && !type.isBlank()) ? type : null;

        } else {
            this.title = "Event Details";
            this.platform = null;
            this.link = null;
            this.interviewerName = null;
            this.interviewType = null;
        }
    }

    /** Returns the window title string. */
    public String getTitle() {
        return title;
    }

    /** Returns the event location. */
    public String getLocation() {
        return location;
    }

    /** Returns the formatted date-time string. */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * Returns the assessment platform, or {@code null} if this is not an
     * {@link OnlineAssessment}.
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * Returns the assessment link, or {@code null} if this is not an
     * {@link OnlineAssessment}.
     */
    public String getLink() {
        return link;
    }

    /**
     * Returns the interviewer's name, or {@code null} if this is not an
     * {@link Interview} or if the field was left blank.
     */
    public String getInterviewerName() {
        return interviewerName;
    }

    /**
     * Returns the interview type, or {@code null} if this is not an
     * {@link Interview} or if the field was left blank.
     */
    public String getInterviewType() {
        return interviewType;
    }
}
