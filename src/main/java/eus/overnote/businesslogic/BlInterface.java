package eus.overnote.businesslogic;

import eus.overnote.domain.OvernoteUser;

public interface BlInterface {
    OvernoteUser registerUser(String fullName, String email, String password, String confirmPassword);
    OvernoteUser loginUser(String email, String password);
}