package eus.overnote.data_access;

import eus.overnote.domain.OvernoteUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

    public OvernoteUser loginUser(String email, String password) {
        try {
            TypedQuery<OvernoteUser> query = db.createQuery("SELECT u FROM OvernoteUser u WHERE u.email = :email AND u.password = :password", OvernoteUser.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
            if (query.getResultList().isEmpty()) {
                logger.info("User with email \"{}\" not found or password incorrect", email);
                return null;
            }
            logger.info("User with email \"{}\" logged in successfully", email);
            return query.getSingleResult();
        } catch (Exception e) {
            db.getTransaction().rollback();
            logger.error("Error logging in user: {}", e.getMessage());
            return null;
        }
    }
}
