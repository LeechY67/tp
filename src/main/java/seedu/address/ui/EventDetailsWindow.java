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
 * <p>All label-population logic lives in {@link EventDetailsViewModel};
 * this class is a thin JavaFX wrapper that binds the view-model's values
 * to the FXML labels.
 */
public class EventDetailsWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(EventDetailsWindow.class);
    private static final String FXML = "EventDetailsWindow.fxml";

    @FXML private Label titleLabel;
    @FXML private Label locationLabel;
    @FXML private Label dateTimeLabel;
    @FXML private Label platformLabel;
    @FXML private Label linkLabel;
    @FXML private Label notesLabel;

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
     * derived from the given {@code ApplicationEvent}.
     *
     * @param event the event whose details are to be displayed.
     */
    public void setEventDetails(ApplicationEvent event) {
        EventDetailsViewModel vm = new EventDetailsViewModel(event);
        titleLabel.setText(vm.getTitle());
        locationLabel.setText(vm.getLocation());
        dateTimeLabel.setText(vm.getDateTime());
        platformLabel.setText(vm.getPlatform());
        linkLabel.setText(vm.getLink());
        notesLabel.setText(vm.getNotes());
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
