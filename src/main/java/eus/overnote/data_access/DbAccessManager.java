package eus.overnote.data_access;

import eus.overnote.domain.Note;
import eus.overnote.domain.OvernoteUser;
import eus.overnote.domain.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class DbAccessManager {

    private static final Logger logger = LoggerFactory.getLogger(DbAccessManager.class);
    protected EntityManager db;
    protected EntityManagerFactory emf;

    public DbAccessManager() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            emf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            logger.error("Error creating database connection: {}", e.getMessage());
            StandardServiceRegistryBuilder.destroy(registry);
        }

        db = emf.createEntityManager();
        logger.info("Database connection established");

    }

    public void close() {
        db.close();
        logger.info("Database connection closed");
    }

    public List<Note> getNotesbyUserID() {
        try {
            logger.info("Retrieving notes for user with id {}", getSession().getCurrentUser().getId());

        }
        catch (Exception e){
            logger.error("Error getting user ID: {}", e.getMessage());
            return null;
        }
        try {
            TypedQuery<Note> query = db.createQuery("SELECT n FROM Note n WHERE n.user.id = :userId", Note.class);
            query.setParameter("userId", getSession().getCurrentUser().getId());
            List<Note> notes = query.getResultList();
            logger.info("Notes retrieved successfully");
            return notes;
        } catch (Exception e) {
            logger.error("Error retrieving notes: {}", e.getMessage());
            return null;
        }

    }

    public Session getSession() {
        try {
            TypedQuery<Session> query = db.createQuery("select s from Session as s", Session.class);
            Session session;
            db.getTransaction().begin();
            if (query.getResultList().isEmpty()) {
                session = new Session();
                session = db.merge(session);
                logger.info("Session not found, creating a new one");
                db.getTransaction().commit();
                return session;
            } else {
                session = query.getSingleResult();
                logger.info("Session found");
                db.getTransaction().commit();
                return session;
            }
        } catch (Exception e) {
            db.getTransaction().rollback();
            logger.error("Error getting session: {}", e.getMessage());
            return null;
        }
    }

    public void saveSession(Session session) {
        try {
            db.getTransaction().begin();
            db.merge(session);
            db.getTransaction().commit();
            logger.info("Session saved successfully");
        } catch (Exception e) {
            db.getTransaction().rollback();
            logger.error("Error saving session: {}", e.getMessage());
        }
    }

    public OvernoteUser registerUser(String fullName, String email, String password) {
        try {
            TypedQuery<OvernoteUser> query = db.createQuery("SELECT u FROM OvernoteUser u WHERE u.email = :email", OvernoteUser.class);
            query.setParameter("email", email);
            if (!query.getResultList().isEmpty()) {
                logger.info("User with email \"{}\" already exists", email);
                return null;
            }

            db.getTransaction().begin();
            OvernoteUser user = new OvernoteUser(fullName, email, password);
            user = db.merge(user);
            db.persist(user);
            db.getTransaction().commit();
            logger.info("User with email \"{}\" registered successfully", email);
            return user;
        } catch (Exception e) {
            db.getTransaction().rollback();
            logger.error("Error registering user: {}", e.getMessage());
            return null;
        }
    }

    public OvernoteUser getUserByEmail(String email) {
        try {
            TypedQuery<OvernoteUser> query = db.createQuery("SELECT u FROM OvernoteUser u WHERE u.email = :email", OvernoteUser.class);
            query.setParameter("email", email);
            if (query.getResultList().isEmpty()) {
                logger.info("User with email \"{}\" not found", email);
                return null;
            }
            logger.info("User with email \"{}\" found", email);
            return query.getSingleResult();
        } catch (Exception e) {
            db.getTransaction().rollback();
            logger.error("Error getting user by email: {}", e.getMessage());
            return null;
        }
    }

    public void saveNote(Note note) {
        if (!(note.getId() == null)) {


        try {
            db.getTransaction().begin();
            logger.debug("Transaction to save a note started");
            note = db.merge(note);
            note.getUser().getNotes().add(note);
            db.getTransaction().commit();
            logger.info("Note with id {} saved successfully", note.getId());
        } catch (Exception e) {
            db.getTransaction().rollback();
            logger.error("Error saving note: {}", e.getMessage());
        }
        }
        else {
            logger.error("Note id is null");
        }
    }

    public void updateNote(Note note) {
        try {

            logger.error(" last Updating note with id {}", note.getId());
            db.getTransaction().begin();
            db.merge(note);
            db.getTransaction().commit();
            logger.info("Note with id {} updated successfully", note.getId());
        } catch (Exception e) {
            db.getTransaction().rollback();
            logger.error("Error updating note: {}", e.getMessage());
        }
    }

    public void moveNoteToTrash(Note note) {
        try {
            db.getTransaction().begin();
            note.moveToTrash();
            db.merge(note);
            db.getTransaction().commit();
            logger.info("Note with id {} marked as deleted successfully", note.getId());
        } catch (Exception e) {
            db.getTransaction().rollback();
            logger.error("Error marking note as deleted: {}", e.getMessage());
        }
    }

    public void deleteNote(Note note) {
        try {
            db.getTransaction().begin();
            note.getUser().getNotes().remove(note);
            db.remove(note);
            db.getTransaction().commit();
            logger.info("Note with id {} deleted successfully", note.getId());
        } catch (Exception e) {
            db.getTransaction().rollback();
            logger.error("Error deleting note: {}", e.getMessage());
        }
    }
}
