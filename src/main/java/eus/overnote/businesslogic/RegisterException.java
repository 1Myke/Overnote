package eus.overnote.businesslogic;

public class RegisterException extends RuntimeException {
  boolean emailValid;
    boolean isPasswordValid;
    boolean isPasswordsMatch;



  public RegisterException(String message, boolean isEmailValid, boolean isPasswordValid, boolean isPasswordsMatch) {
    super(message);
    this.emailValid = isEmailValid;
    this.isPasswordValid = isPasswordValid;
    this.isPasswordsMatch = isPasswordsMatch;
  }
  public boolean isEmailValid() {return emailValid;}

  public boolean isPasswordValid() {
    return isPasswordValid;
  }

  public boolean isPasswordsMatch() {
    return isPasswordsMatch;
  }
}
