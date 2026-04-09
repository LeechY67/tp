package seedu.address.model.application;

import java.time.LocalDateTime;

/**
 * Represents an interview event arranged during the application process.
 * Interviewer name and interview type are optional fields.
 */
public class Interview extends ApplicationEvent {

    /** Default value used when interviewer name is not provided. */
    public static final String DEFAULT_INTERVIEWER = "";

    /** Default value used when interview type is not provided. */
    public static final String DEFAULT_INTERVIEW_TYPE = "";

    private final String interviewerName;
    private final String interviewType;

    /**
     * Constructs an {@code Interview} with all fields specified.
     *
     * @param location       location of the interview (required)
     * @param dateTime       date and time of the interview (required)
     * @param interviewerName name of the interviewer (optional)
     * @param interviewType  type of interview, e.g. "behavioural", "technical" (optional)
     */
    public Interview(String location, LocalDateTime dateTime, String interviewerName, String interviewType) {
        super(location, dateTime);
        this.interviewerName = interviewerName != null ? interviewerName : DEFAULT_INTERVIEWER;
        this.interviewType = interviewType != null ? interviewType : DEFAULT_INTERVIEW_TYPE;
    }

    /**
     * Constructs an {@code Interview} with only the required fields (location and dateTime).
     * Interviewer name and interview type default to empty strings.
     *
     * @param location  location of the interview
     * @param dateTime  date and time of the interview
     */
    public Interview(String location, LocalDateTime dateTime) {
        this(location, dateTime, DEFAULT_INTERVIEWER, DEFAULT_INTERVIEW_TYPE);
    }

    /**
     * Returns the interviewer's name, or an empty string if not set.
     */
    public String getInterviewerName() {
        return interviewerName;
    }

    /**
     * Returns the type of interview, or an empty string if not set.
     */
    public String getInterviewType() {
        return interviewType;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Interview)) {
            return false;
        }
        Interview otherInterview = (Interview) other;
        return getLocation().equals(otherInterview.getLocation())
                && getLocalDate().equals(otherInterview.getLocalDate())
                && interviewerName.equals(otherInterview.interviewerName)
                && interviewType.equals(otherInterview.interviewType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Interview{location=").append(getLocation());
        sb.append(", dateTime=").append(getLocalDate());
        if (!interviewerName.isEmpty()) {
            sb.append(", interviewer=").append(interviewerName);
        }
        if (!interviewType.isEmpty()) {
            sb.append(", type=").append(interviewType);
        }
        sb.append("}");
        return sb.toString();
    }
}
