package eus.overnote.presentation.views;

import eus.overnote.businesslogic.BlInterface;
import eus.overnote.businesslogic.BusinessLogic;
import eus.overnote.domain.Note;
import eus.overnote.presentation.WindowManager;
import eus.overnote.presentation.components.NoteEditorController;
import eus.overnote.presentation.components.NoteThumbnailController;
import eus.overnote.presentation.components.ProfileBannerController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Locale;

public class MainApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(MainApplicationController.class);
    private final BlInterface bl = BusinessLogic.getInstance();
    private NoteEditorController noteEditorController;
    private boolean viewingTrash = false;

    @FXML
    private VBox sidebarVBox;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button newNoteAiButton;

    @FXML
    private BorderPane root;

    @FXML
    private Button newNoteButton;

    @FXML
    private MenuButton profileMenuButton;

    @FXML
    private MenuItem en;

    @FXML
    private MenuItem es;

    @FXML
    private MenuItem eu;

    private ObservableList<Note> notes;
    private ObservableList<Node> thumbnails;

    public void initialize() {
        logger.debug("Initializing main application view");
        bl.checkNotesForDeletion();
        noteEditorController = bl.getNoteEditorController();

        // Initialize note list
        notes = FXCollections.observableArrayList(bl.getNotesFromUserId());

        // Load the profile banner FXML
        try {
            profileMenuButton.setGraphic(FXMLLoader.load(ProfileBannerController.class.getResource("profilebanner.fxml")));
            logger.debug("Profile banner loaded");
        } catch (Exception e) {
            profileMenuButton.setGraphic(new Text("Error loading profile banner"));
            logger.error("Error loading profile banner", e);
        }

        // Load Note FXML
        root.setCenter(WindowManager.getInstance().getNoteEditorParent());

        // Initialize the sidebar
        thumbnails = bl.getThumbnails();
        Bindings.bindContent(sidebarVBox.getChildren(), thumbnails);

        // Sort from oldest to newest
        notes.sort(new Comparator<Note>() {
            @Override
            public int compare(Note n1, Note n2) {
                if (n1.getCreationDate() == null || n2.getCreationDate() == null) {
                    return 0;
                }
                return n1.getCreationDate().compareTo(n2.getCreationDate());
            }
        });

        // Add thumbnails to the sidebar
        notes.forEach(note -> {
            bl.addNewThumbnail(note);
            if (note.isDeleted()){
                NoteThumbnailController thumbnail = bl.getThumbnailController(note);
                thumbnail.hide();
            }
        });

        // Bind the searchbar with the visible thumbnails
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            notes.forEach(note -> {
                NoteThumbnailController thumbnail = bl.getThumbnailController(note);
                if (note.matchesContent(newValue) && (!note.isDeleted() ^ viewingTrash)) {
                    thumbnail.show();
                } else {
                    thumbnail.hide();
                }
            });
        });

        // Language menu listeners
        en.setOnAction(event -> bl.changeLanguage(Locale.ENGLISH));
        es.setOnAction(event -> bl.changeLanguage(new Locale("es")));
        eu.setOnAction(event -> bl.changeLanguage(new Locale("eu")));
    }

    /**
     * Calls the controller of the note editor to save the noted that
     * was previously selected. Calls the business logic to select the
     * new note.
     *
     * @param note the note to select.
     */
    private void selectNote(Note note) {
        if (bl.getLoggedInUser() == null) {
            logger.error("No user logged in");
            return;
        }

        // Save the previous note
        logger.info("Saving the current note before selecting the next.");
        noteEditorController.updateNote();

        logger.info("Selecting note with id\"{}\"", note.getId());

        bl.selectNote(note);
    }

    /**
     * This method is called when the button for creating notes is pressed.
     * Creates a new instance of {@link Note} and calls the business logic to
     * save it in the database. It automatically selects the created note.
     *
     * @param event the {@link ActionEvent} created on the interaction.
     */
    @FXML
    void createNote(ActionEvent event) {
        logger.info("Saving the current note before creating the new.");
        noteEditorController.updateNote();

        logger.debug("Creating new note");
        Note createdNote = new Note(bl.getTranslation("note.default.title"), "", bl.getLoggedInUser());
        bl.saveNote(createdNote);
        notes.add(createdNote);
        bl.addNewThumbnail(createdNote);
        Platform.runLater(() -> selectNote(createdNote));
    }

    @FXML
    void createAINote(ActionEvent event) {
        logger.info("Saving the current note before creating the new.");
        noteEditorController.updateNote();

        logger.debug("Creating new AI generated note");

        // Show a dialog asking prompt for the AI note
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(bl.getTranslation("main.ai.note.create.dialog.title"));
        dialog.setHeaderText(null);
        dialog.setContentText(bl.getTranslation("main.ai.note.create.dialog.content"));
        dialog.showAndWait().ifPresent(prompt -> {
            logger.debug("Creating new AI note with prompt: \"{}\"", prompt);

            // Show progress indicator
            ProgressIndicator progressIndicator = new ProgressIndicator();
            progressIndicator.setMaxSize(50, 50);
            Alert progressAlert = new Alert(Alert.AlertType.NONE);
            progressAlert.setTitle(bl.getTranslation("main.ai.note.generating.dialog.title"));
            progressAlert.setGraphic(progressIndicator);
            progressAlert.setHeaderText(bl.getTranslation("main.ai.note.generating.dialog.content"));
            progressAlert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            // Create background task
            javafx.concurrent.Task<Note> task = new javafx.concurrent.Task<>() {
                @Override
                protected Note call() throws Exception {
                    return bl.generateAINote(prompt);
                }
            };

            // Handle task completion
            task.setOnSucceeded(e -> {
                progressAlert.close();
                Note generatedNote = task.getValue();
                bl.saveNote(generatedNote);
                notes.add(generatedNote);
                bl.addNewThumbnail(generatedNote);
                Platform.runLater(() -> selectNote(generatedNote));
            });

            // Handle task failure
            task.setOnFailed(e -> {
                progressAlert.close();
                logger.error("Error generating AI note: {}", task.getException().getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(bl.getTranslation("main.ai.note.error.title"));
                alert.setHeaderText(null);
                alert.setContentText(bl.getTranslation("main.ai.note.error.content"));
                alert.showAndWait();
            });

            // Start the task in a new thread
            new Thread(task).start();
            progressAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.CANCEL && task.isRunning()) {
                    task.cancel();
                }
            });
        });

    }

    /**
     * This method is called when the log out option is selected from the
     * profile menu. It calls the business logic to log out the user and
     * navigates to the login view.
     *
     * @param event the {@link ActionEvent} created on the interaction.
     */
    @FXML
    void onLogout(ActionEvent event) {
        // Erase notes "dropdown" menu

        logger.debug("Logging out user \"{}\"", bl.getLoggedInUser().getFullName());
        bl.logoutUser();
        WindowManager.getInstance().navigateToLogin();
    }

    @FXML
    void focusSearchBar(MouseEvent event) {
        searchTextField.requestFocus();
    }

    @FXML
    void configureGemini(ActionEvent event) {
        logger.debug("Configuring Gemini");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Gemini Configuration");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter the Gemini API key:");
        dialog.showAndWait().ifPresent(bl::setGeminiAPIKey);
    }

    @FXML
    void toggleTrashView(ActionEvent event) {
        logger.debug("Toggling trash view");
        viewingTrash = !viewingTrash;
        if (viewingTrash) {
            newNoteButton.setDisable(true);
            newNoteAiButton.setDisable(true);
            notes.forEach(note -> {
                NoteThumbnailController thumbnail = bl.getThumbnailController(note);
                if (note.isDeleted()) {
                    thumbnail.show();
                } else {
                    thumbnail.hide();
                }
            });
        } else {
            newNoteButton.setDisable(false);
            newNoteAiButton.setDisable(false);
            notes.forEach(note -> {
                NoteThumbnailController thumbnail = bl.getThumbnailController(note);
                if (note.isDeleted()) {
                    thumbnail.hide();
                } else {
                    thumbnail.show();
                }
            });
        }
    }
}
