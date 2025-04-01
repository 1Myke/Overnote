package eus.overnote.businesslogic;

import eus.overnote.domain.OvernoteUser;
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
    public void testRegisterUser() {


        String email = "john.doe@example.com";
        String password = "Password123";
        String confirmPassword = "Password123";

        Assert.assertTrue(businessLogic.validateEmail(email));
        Assert.assertTrue(businessLogic.validatePassword(password));
        Assert.assertTrue(businessLogic.validatePasswordMatch(password, confirmPassword));

    }
    @Test
    public void testValidEmail(){
        String email = "john.doe@example.com";
        Assert.assertTrue(businessLogic.validateEmail(email));

    }
    @Test
    public void testValidPassword(){
        String password = "Password123";
        Assert.assertTrue(businessLogic.validatePassword(password));
    }
    @Test
    public void testValidPasswordMatch(){
        String password = "Password123";
        String confirmPassword = "Password123";
        Assert.assertTrue(businessLogic.validatePasswordMatch(password, confirmPassword));
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
