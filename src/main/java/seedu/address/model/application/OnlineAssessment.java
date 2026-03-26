package seedu.address.model.application;


import java.time.LocalDateTime;

/**
 * Represents online assessment that is appointed during the application
 */
public class OnlineAssessment extends ApplicationEvent {
    public static final String EMPTY_NOTES_VALUE = "No notes set";
    private final String platform;
    private final String link;
    private final String notes;

    /**
     * Constructs an OnlineAssessment class
     * @param location location of online assessment
     * @param platform platform of online assessment
     * @param link link of online assessment
     * @param notes extra notes of online assessment
     */
    public OnlineAssessment(String location, LocalDateTime dateTime, String platform,
                            String link, String notes) {
        super(location, dateTime);
        this.platform = platform;
        this.link = link;
        this.notes = notes;
    }

    /**
     * Constructs an OnlineAssessment class
     * @param location location of online assessment
     * @param platform platform of online assessment
     * @param link link of online assessment
     */
    public OnlineAssessment(String location, LocalDateTime dateTime, String platform, String link) {
        super(location, dateTime);
        this.platform = platform;
        this.link = link;
        this.notes = EMPTY_NOTES_VALUE;
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

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof OnlineAssessment)) {
            return false;
        }
        OnlineAssessment otherOnlineAssessment = (OnlineAssessment) other;
        return getLocation().equals(otherOnlineAssessment.getLocation())
                && getLocalDate().equals(otherOnlineAssessment.getLocalDate())
                && platform.equals(otherOnlineAssessment.platform)
                && link.equals(otherOnlineAssessment.link)
                && notes.equals(otherOnlineAssessment.notes);
    }
}


