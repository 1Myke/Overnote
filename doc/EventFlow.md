# Event Flow

## Event Flow / Register in the App

**Basic Flow:**
* Guest provides their full name, email address, and a password (the password is written twice). 
* Guest accepts the Terms and Conditions. 
* System stores the data as a registered user.

**Alternative Flow:**
* If there is already a user registered with that account → Error. End.
* If the two passwords do not match → Error. End.
* If the Terms and Conditions are not accepted → Error. End.

## Event Flow / Login in the App

**Basic Flow:**
* Guest fills in email and password.
* System verifies that the email and password are correct and considers the guest registered.

**Alternative Flow:**
* If the email is not saved in the database → Error. End.
* If the password does not match the one saved with the given email → Error. End.

## Event Flow / Create a Note

**Preconditions:**
* The user is logged in.

**Basic Flow:**
* The user clicks the button to add a new note. 
* System creates a new entity in the database for the user. 
* System displays this new entity (the note) in the main page of the application window.

## Event Flow / Edit a Note

**Preconditions:**
* The user is logged in.
* The note already exists.

**Basic Flow:**
* User clicks a note.
* System searches the selected note in the database. 
* System displays the selected note on screen. 
* User writes in the note. 
* System waits 5 seconds. 
* System updates the database with the new information of the edited note.

**Alternative Flow:**
* If the user clicks the save button, the system does not wait 5 seconds and updates the database immediately.

## Event Flow / AI Use

**Preconditions:**
* The user is logged in. 
* The user has provided their Gemini API key.

**Basic Flow:**
* User selects a line or a paragraph.
* System displays a panel with three buttons: Correct, Rewrite, and Autocomplete.
* User clicks one of the three buttons.
* System calls the Gemini API, and the AI provides an answer.
* System writes the answer in the note.

**Alternative Flow:**
* If the user does not have tokens to use the AI → Error message. End.

## Event Flow / Recover a Note

**Preconditions:**
* The user is logged in.
* The note is in the trash.

**Basic Flow:**
* The user clicks the trash button (or the "Recover Note" button).
* System changes the screen to show deleted notes.
* User selects the note(s) to recover.
* User clicks the button to recover the note.
* System moves the note from the trash back to the main notes screen.

## Event Flow / Import a Note

**Preconditions:** 
* The user is logged in.

**Basic Flow:**
* User selects the import option. 
* User provides the file path to import. 
* System adds the file to the database and displays it with the other notes.

**Alternative Flow:**
* If the file does not exist → Error. End. 
* If the note already exists → Error. End.

## Event Flow / Export a Note

**Preconditions:** 
* The user is logged in.

**Basic Flow:**
* User selects the export option.
* User selects the file to export.
* User provides the location to save the exported file.
* System retrieves the note from the database and saves it in the given location.

**Alternative Flow:**
* If the location does not exist → Error. End.

## Event Flow / Save Note

**Preconditions:** 
* The user is logged in.

**Basic Flow:**
* The user clicks the save button to save the note.
* System detects when the user is not typing for 5 seconds and saves automatically.
* System replaces the old note with the newly saved one.
* System displays a "Successfully Saved" label on the screen.

**Alternative Flow:**
* If the user does not click the "Save Note" button before leaving the note, the changes are discarded.

## Event Flow / Delete Note

**Preconditions:**
* The user is logged in.
* The note already exists.

**Basic Flow:**
* The user selects the note they want to delete.
* The user clicks the red delete button.
* System moves the note to the trash.

**Non-functional requirements:** 
* The note will be permanently deleted after 30 days if the user takes no action.

## Event Flow / Logout

**Preconditions:** 
* The user is logged in.

**Basic Flow:**
* User selects the logout option.
* System closes the main window and displays the login window.
