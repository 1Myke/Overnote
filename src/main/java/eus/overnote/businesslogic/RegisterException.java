package eus.overnote.businesslogic;

public class RegisterException extends RuntimeException {
  boolean emailValid;
    boolean PasswordValid;
    boolean PasswordsMatch;



  public RegisterException(String message, boolean isEmailValid, boolean isPasswordValid, boolean isPasswordsMatch) {
    super(message);
    this.emailValid = isEmailValid;
    this.PasswordValid = isPasswordValid;
    this.PasswordsMatch = isPasswordsMatch;
  }
  public boolean isEmailValid() {return emailValid;}

  public boolean isPasswordValid() {
    return PasswordValid;
  }

  public boolean isPasswordsMatch() {
    return PasswordsMatch;
  }
}
