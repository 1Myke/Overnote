package eus.overnote.businesslogic;

import eus.overnote.data_access.DbAccessManager;
import eus.overnote.domain.OvernoteUser;
import org.hibernate.sql.ast.tree.expression.Over;

public class BusinessLogic implements BlInterface {
    @Override
    public OvernoteUser registerUser(String fullName, String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return null;
        }

        DbAccessManager db = new DbAccessManager();
        OvernoteUser createdUser = db.registerUser(fullName, email, password);
        db.close();
        return createdUser;
    }

    @Override
    public OvernoteUser loginUser(String email, String password) {
        DbAccessManager db = new DbAccessManager();
        OvernoteUser loggedInUser = db.loginUser(email, password);
        db.close();
        return loggedInUser;
    }
}
