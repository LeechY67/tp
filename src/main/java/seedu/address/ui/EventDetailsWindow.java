package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.application.ApplicationEvent;

/**
 * Window that displays the details of an {@code ApplicationEvent}.
 *
 * <p>Supports two concrete event types:
 * <ul>
 *   <li>{@code OnlineAssessment} – shows Platform and Link rows.</li>
 *   <li>{@code Interview} – shows Interviewer and Interview Type rows,
 *       hiding either row when its optional value was not provided.</li>
 * </ul>
 *
 * <p>All label-population and branching logic lives in
 * {@link EventDetailsViewModel}; this class is a thin JavaFX wrapper that
 * binds the view-model's values to the FXML labels and controls row
 * visibility.
 */
public class EventDetailsWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(EventDetailsWindow.class);
    private static final String FXML = "EventDetailsWindow.fxml";

    // ── Always-visible labels ────────────────────────────────────────────────
    @FXML private Label titleLabel;
    @FXML private Label locationLabel;
    @FXML private Label dateTimeLabel;

    // ── OnlineAssessment rows ────────────────────────────────────────────────
    @FXML private Label platformKey;
    @FXML private Label platformLabel;
    @FXML private Label linkKey;
    @FXML private Label linkLabel;

    // ── Interview rows ───────────────────────────────────────────────────────
    @FXML private Label interviewerKey;
    @FXML private Label interviewerLabel;
    @FXML private Label interviewTypeKey;
    @FXML private Label interviewTypeLabel;

    /**
     * Creates a new {@code EventDetailsWindow} backed by the supplied {@code Stage}.
     */
    public EventDetailsWindow(Stage root) {
        super(FXML, root);
    }

    /**
     * Creates a new {@code EventDetailsWindow} with a fresh {@code Stage}.
     */
    public EventDetailsWindow() {
        this(new Stage());
    }

    /**
     * Populates the window labels using an {@link EventDetailsViewModel}
     * derived from the given {@code ApplicationEvent}, and shows or hides
     * rows that are not relevant to the event type.
     *
     * @param event the event whose details are to be displayed.
     */
    public void setEventDetails(ApplicationEvent event) {
        EventDetailsViewModel vm = new EventDetailsViewModel(event);

        // Always-visible fields
        titleLabel.setText(vm.getTitle());
        locationLabel.setText(vm.getLocation());
        dateTimeLabel.setText(vm.getDateTime());

        // OnlineAssessment rows: show only when platform/link are non-null
        setRowVisible(platformKey, platformLabel, vm.getPlatform());
        setRowVisible(linkKey, linkLabel, vm.getLink());

        // Interview rows: show only when the optional value is non-null
        setRowVisible(interviewerKey, interviewerLabel, vm.getInterviewerName());
        setRowVisible(interviewTypeKey, interviewTypeLabel, vm.getInterviewType());
    }

    /**
     * Shows or hides a key-value row pair depending on whether {@code value}
     * is non-null.
     *
     * <p>Setting both {@code managed} and {@code visible} to {@code false}
     * removes the row from the GridPane's layout so it takes up no space.
     *
     * @param keyLabel   the label on the left (e.g. "Platform:")
     * @param valueLabel the label on the right (e.g. the actual platform text)
     * @param value      the text to display, or {@code null} to hide the row
     */
    private void setRowVisible(Label keyLabel, Label valueLabel, String value) {
        boolean show = (value != null);
        keyLabel.setVisible(show);
        keyLabel.setManaged(show);
        valueLabel.setVisible(show);
        valueLabel.setManaged(show);
        if (show) {
            valueLabel.setText(value);
        }
    }

    /** Shows the event details window. */
    public void show() {
        logger.fine("Showing event details window.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /** Returns true if the event details window is currently showing. */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /** Focuses on the event details window. */
    public void focus() {
        getRoot().requestFocus();
    }

    /** Hides the event details window. */
    public void hide() {
        getRoot().hide();
    }

    /** Handles the close button click. */
    @FXML
    private void handleClose() {
        hide();
    }
}
