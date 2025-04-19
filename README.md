# Overnote

> [!WARNING]
> **Overnote is in early development**. Features may change unexpectedly
> and frequent updates are anticipated.

## Overview

Welcome to Overnote! 🎉 Your ultimate multiplatform note-taking companion designed to boost
your productivity and keep you organized. With Overnote, you can enjoy features like rich
text editing, AI-assisted note generation[^1], and powerful search and filtering capabilities.

[^1]: AI-assisted note generation is powered by Google's Gemini technology.

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
The name of our group is *Stack Underflow* and the members are, Jorge Arévalo, Mikel López, Aimar Villegas and Mikel Martinez.

## Sprint 1
We've been working on some different points for this sprint. The main stuff of this project was the preparation and presentation of the project, that involves different parts as, the UI mockup, the wording, the use-case diagram, the domain model or the event flow. After that we were asked to start coding and to implement the Register/Login use-case and another use-case that we want to implement. We choose the "create a note" use-case, mainly because is the first action that the user would do after logging in in our application and also because without the creation of the note we can't continue working on some of the other use-cases.

## Sprint 2
For this second sprint of the project, we've implemented a security feature for storing passwords as hashes. This was done using the Bcrypt library. To enhance password security, we now require users to create robust passwords by applying regular expressions.
In addition, two new use cases were implemented in Overnote. One allows the user to edit notes, making note usage more comfortable. The other allows the user to delete a note. In this last case, the user can also access the bin. For the next sprint, we would like to implement the "recover a note" use case, which will allow users to restore a note from the bin within 30 days of deletion.
Finally, we have improved and fixed the errors from the first sprint and created the sequence diagram for the "Edit a note" use case.

# Sprint 2 PD:
There is an error in the creation of the notes that we need to fix for the 3rd sprint. Notes are duplicated when we create one, in other words, when you create one note in the databes two notes are created and we are still working on the fix of this
