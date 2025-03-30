package eus.overnote.businesslogic;

import eus.overnote.data_access.DbAccessManager;
import eus.overnote.domain.Note;
import eus.overnote.domain.OvernoteUser;
import lombok.Getter;
import org.mindrot.jbcrypt.BCrypt;

public class BusinessLogic implements BlInterface {

    private static BusinessLogic instance;
    private final DbAccessManager db;
    @Getter
    private OvernoteUser loggedInUser;

    private BusinessLogic() {
        db = new DbAccessManager();
    }

    public static BusinessLogic getInstance() {
        if (instance == null) {
            instance = new BusinessLogic();
        }
        return instance;
    }

    /*
     boolean problem = false;

        Pattern Email = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher m = Email.matcher(email);
        boolean emailValid = m.matches();
        if (!emailValid) {
            errorInEmail.setText("Invalid email");
            errorInEmail.setStyle("-fx-text-fill: red;");
            logger.debug("Error in email, Invalid email");
            problem = true;
            }
        else{
            errorInEmail.setText("");
        }
        Pattern Password = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
        m = Password.matcher(password);
        boolean passwordValid = m.matches();
        if (!passwordValid) {
            errorInPassword.setText("Password must contain at least 8 characters, including UPPER/lowercase and numbers");
            errorInPassword.setStyle("-fx-text-fill: red;");
            logger.debug("Error in password, Specification not met");
            problem = true;
        }
        else{
            errorInPassword.setText("");
        }
        if (problem) {
            return;
        }



     */
    @Override
    public boolean validateEmail(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
    @Override
    public boolean validatePassword(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
    }
    @Override
    public boolean validatePasswordMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }


    @Override
    public OvernoteUser registerUser(String fullName, String email, String password, String confirmPassword) throws RegisterException {
        boolean validEmail = true;
        boolean validPassword = true;
        boolean passwordsMatch  = validatePasswordMatch(password, confirmPassword);

        if (!validateEmail(email)) {
            validEmail = false;
        }

        if (!validatePassword(password)) {
            validPassword = false;
        }
        if (!validEmail || !validPassword || !passwordsMatch) {
            throw new RegisterException("Invalid registration data", validEmail, validPassword, passwordsMatch);
        }

        String hashedPassword = hashPassword(password);
        OvernoteUser user = db.registerUser(fullName, email, hashedPassword);
        loggedInUser = user;
        return user;
    }

    @Override
    public OvernoteUser loginUser(String email, String password) {
        OvernoteUser user = db.getUserByEmail(email);
        if (user == null) {
            return null;
        }
        if (checkPassword(password, user.getPassword())) {
            loggedInUser = user;
            return user;
        } else
            return null;
    }

    @Override
    public boolean isUserLoggedIn() {
        return loggedInUser != null;
    }

    @Override
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    @Override
    public boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    @Override
    public void saveNote(Note note) {
        db.saveNote(note);
    }

    @Override
    public void updateNote(Note note) {
        db.updateNote(note);
    }

    @Override
    public void deleteNote(Note note) {
        db.deleteNote(note);
    }
}
