package seedu.address.model.application;


import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents any event that is arranged during the application
 * Can either be an interview or an online assessment
 */
public abstract class ApplicationEvent {
    public static final String DATETIME_CONSTRAINTS =
            "DateTime should not be blank and should ideally follow yyyy-MM-dd HH:mm format";
    private final String location;
    private final LocalDateTime dateTime;

    /**
     * Constructs an ApplicationEvent class with dateTime and location parameters
     *
     * @param location location of event
     */
    public ApplicationEvent(String location, LocalDateTime dateTime) {
        this.location = location;
        this.dateTime = dateTime;
    }


    /**
     * Returns true if a given string is a valid datetime.
     * <p>
     * Valid values are:
     * <ul>
     *     <li>{@code "yyyy-MM-dd HH:mm"}</li>
     * </ul>
     *
     * @throws NullPointerException if {@code test} is {@code null}
     */
    public static boolean isValidDateTime(String test) {
        Objects.requireNonNull(test);
        if (test.isBlank()) {
            return false;
        }

        return test.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}");
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getLocalDate() {
        return dateTime;
    }
}

