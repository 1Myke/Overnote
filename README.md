# Overnote
![Static Badge](https://img.shields.io/badge/View%20presentation-Google%20Slides-yellow?style=for-the-badge&link=https%3A%2F%2Fdocs.google.com%2Fpresentation%2Fd%2F1G7LYpwR0kCgq6G4kE5DWdMPRvlt3iWKQlWVVLRXFJzQ%2Fedit%3Fusp%3Dsharing)

## Overview

Welcome to Overnote! 🎉 Your ultimate multiplatform note-taking companion designed to boost
your productivity and keep you organized. With Overnote, you can enjoy features like rich
text editing, AI-assisted note generation[^1], and powerful search and filtering capabilities.

[^1]: AI-assisted note generation is powered by Google's Gemini technology. Bring your own key.

## Features

1. 📝 **Creating Notes:** Start fresh or use predefined templates to create new notes.
2. 🏷️ **Filtering Notes:** Use tags to filter notes and access them effortlessly.
3. 🔍 **Searching Notes:** Find what you need with real-time suggestions.
4. ✏️ **Editing Notes:** Enjoy auto-save and basic formatting options while editing.
5. 🤖 **AI Assistant Integration:** Let our AI help you with rewriting, content generation, grammar correction, and summarization.
6. 🗑️ **Deleting Notes:** Move notes to a trash bin with a 30-day recovery period.
7. 💾 **Backup and Recovery:** Regular backups, export/import options, and version history to keep your notes safe.

## About us

We are a young group of students from the Basque Country University, knwon as EHU/UPV. We are on our second course of computer science engineering in Donosti/San Sebastian and we are working on this project for the Software Engineering 1 subject.
The name of our group is _Stack Underflow_ and the members are, Jorge Arévalo, Mikel López, Aimar Villegas and Mikel Martinez.

## Sprint 1

We've been working on some different points for this sprint. The main stuff of this project was the preparation and presentation of the project, that involves different parts as, the UI mockup, the wording, the use-case diagram, the domain model or the event flow. After that we were asked to start coding and to implement the Register/Login use-case and another use-case that we want to implement. We choose the "create a note" use-case, mainly because is the first action that the user would do after logging in in our application and also because without the creation of the note we can't continue working on some of the other use-cases.

## Sprint 2

For this second sprint of the project, we've implemented a security feature for storing passwords as hashes. This was done using the Bcrypt library. To enhance password security, we now require users to create robust passwords by applying regular expressions.
In addition, two new use cases were implemented in Overnote. One allows the user to edit notes, making note usage more comfortable. The other allows the user to delete a note. In this last case, the user can also access the bin. For the next sprint, we would like to implement the "recover a note" use case, which will allow users to restore a note from the bin within 30 days of deletion.
Finally, we have improved and fixed the errors from the first sprint and created the sequence diagram for the "Edit a note" use case.

### Sprint 2 PD:

There is an error in the creation of the notes that we need to fix for the 3rd sprint. Notes are duplicated when we create one, in other words, when you create one note in the databes two notes are created and we are still working on the fix of this

## Sprint 3
Finally, for this last sprint, we added the i18n support to change between 3 languages, English, Spanish, and Basque. We also added note formatting support in HTML; the editor is much better now. The option to save the note and to recover the note after deleting has also been added in this sprint, followed by a searching bar for the notes, an application icon, and the much-desired AI-generated note. In this sprint, we also corrected the error of the duplicated notes. To finish the project, we will create the executable to make the application available for the people who want to use it.

## Development

To run the application in development mode, you can use the following command:

```bash
mvn javafx:run
```

### Debug Mode

To run the application in debug mode, you need to modify the `pom.xml` file to use the `DebugMain` class instead of the regular `Main` class. Here's how to do it:

1. Open `pom.xml`
2. Find the `javafx-maven-plugin` configuration
3. Change the `mainClass` from `eus.overnote.Main` to `eus.overnote.DebugMain`

The debug mode will automatically create and log in a debug user with the following credentials:

- Email: `debug@overnote.eus`
- Password: `debug`

### Test Credentials

For testing purposes, there are two sets of credentials available:

1. Debug User (automatically created when running in debug mode):

   - Email: `debug@overnote.eus`
   - Password: `debug`

2. Test User (used in unit tests):
   - Email: `john.doe@example.com`
   - Password: `Password123`

Note: The test user credentials are only used in unit tests and do not persist in the system.
