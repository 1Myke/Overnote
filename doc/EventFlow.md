# Event Flow

## Event Flow / Register in the App

**Basic Flow:**
* Guest provides their full name, email address, and a password (entered twice for confirmation).
* Guest accepts the Terms and Conditions.
* System stores the data and registers the guest as a user.

**Alternative Flow:**
* If an account with the provided email already exists → Error. End.
* If the two password entries do not match → Error. End.
* If the Terms and Conditions are not accepted → Error. End.

## Event Flow / Log in to the App

**Basic Flow:**
* Guest enters their email and password.
* System verifies the credentials and logs the guest into their account.

**Alternative Flow:**
* If the email is not registered → Error. End.
* If the password does not match the one associated with the given email → Error. End.

## Event Flow / Create a Note

**Preconditions:**
* The user is logged in.

**Basic Flow:**
* The user clicks the "New Note" button.
* System creates a new note in the database and associates it with the user.
* System displays the new note on the main screen.

## Event Flow / Edit a Note

**Preconditions:**
* The user is logged in.
* The note already exists.

**Basic Flow:**
* User clicks the thumbnail of a note in the side panel.
* System retrieves the selected note from the database.
* System displays an editor for the selected note on screen.
* User edits the note, modifying the title or content.

**Alternative Flow:**
* If the user has not created any notes yet, the sidebar will be empty, and no note can be edited. End.

## Event Flow / AI Use

**Preconditions:**
* The user is logged in.
* The user has provided their Gemini API key in their profile settings.

**Basic Flow:**
* User selects a line or a paragraph.
* System displays a tooltip with three buttons: Correct, Rewrite, and Autocomplete.
* User clicks one of the three buttons.
* System calls the Gemini API, and the AI provides a response.
* System inserts the AI-generated response into the note.

**Alternative Flow:**
* If the user does not have tokens to use the AI → Error message. End.

## Event Flow / Recover a Note

**Preconditions:**
* The user is logged in.
* The note is in the trash.

**Basic Flow:**
* The user navigates to the trash and selects "Recover Note."
* System changes the screen to show deleted notes.
* User selects the note(s) to recover.
* User clicks the "Recover" button.
* System moves the note from the trash back to the main notes screen.

## Event Flow / Import a Note

**Preconditions:**
* The user is logged in.

**Basic Flow:**
* User selects the import option.
* User selects a file to import.
* System adds the file to the database and displays it with the other notes.

**Alternative Flow:**
* If the file does not exist → Error. End.

## Event Flow / Export a Note

**Preconditions:**
* The user is logged in.

**Basic Flow:**
* User selects the export option.
* User selects a note to export.
* User specifies the location to save the exported file.
* System retrieves the note from the database and saves it in the specified location.

**Alternative Flow:**
* If the system cannot write to the specified location → Error message. End.

## Event Flow / Save Note

**Preconditions:**
* The user is logged in.
* The user is editing a note.

**Basic Flow:**
* The user clicks the save button.
* System updates the edited note in the database.
* System displays a "Successfully Saved" notification.

**Alternative Flow:**
* System detects when the user is inactive for 5 seconds and automatically saves the note.

## Event Flow / Delete Note

**Preconditions:**
* The user is logged in.
* The note already exists.

**Basic Flow:**
* The user selects the note they want to delete.
* The user clicks the red delete button in the edit view.
* System moves the note to the trash.

**Alternative Flow:**
* The user hovers over the note in the sidebar and clicks the trash icon.
* System moves the note to the trash.

**Non-functional requirements:**
* The note will be permanently deleted after 30 days if no action is taken.

## Event Flow / Logout

**Preconditions:**
* The user is logged in.

**Basic Flow:**
* User clicks the profile icon.
* User selects the logout option from the pop-up menu.
* System closes the main window and displays the login screen.
