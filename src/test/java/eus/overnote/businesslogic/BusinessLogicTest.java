package eus.overnote.businesslogic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BusinessLogicTest {

    private BusinessLogic businessLogic;

    @Before
    public void setUp() {
        businessLogic = BusinessLogic.getInstance();
    }

    @Test
    public void testHashPassword() {
        String password = "password";
        String hashedPassword = businessLogic.hashPassword(password);
        Assert.assertNotNull(hashedPassword);
        Assert.assertNotEquals(password, hashedPassword);
    }

    @Test
    public void testCheckPassword() {
        String password = "password";
        String hashedPassword = businessLogic.hashPassword(password);
        boolean isPasswordCorrect = businessLogic.checkPassword(password, hashedPassword);
        Assert.assertTrue(isPasswordCorrect);
    }

    @Test
    public void testCheckPasswordWithWrongPassword() {
        String password = "password";
        String wrongPassword = "wrongPassword";
        String hashedPassword = businessLogic.hashPassword(password);
        boolean isPasswordCorrect = businessLogic.checkPassword(wrongPassword, hashedPassword);
        Assert.assertFalse(isPasswordCorrect);
    }
}
