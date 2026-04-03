package seedu.address.ui;

import java.time.format.DateTimeFormatter;

import seedu.address.model.application.ApplicationEvent;
import seedu.address.model.application.OnlineAssessment;

/**
 * Pure-Java view model for {@link EventDetailsWindow}.
 *
 * <p>Encapsulates all branching logic that decides what text to display for
 * each label, with no dependency on JavaFX. This makes the logic fully
 * unit-testable without a running FX toolkit.
 */
public class EventDetailsViewModel {

    static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

    private final String title;
    private final String location;
    private final String dateTime;
    private final String platform;
    private final String link;
    private final String notes;

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
            this.notes = oa.getNotes();
        } else {
            this.title = "Event Details";
            this.platform = "N/A";
            this.link = "N/A";
            this.notes = "N/A";
        }
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getPlatform() {
        return platform;
    }

    public String getLink() {
        return link;
    }

    public String getNotes() {
        return notes;
    }
}
