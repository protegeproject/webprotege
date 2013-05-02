package edu.stanford.bmir.protege.web.server;

public class EmailConstants {

    public static final String FORGOT_PASSWORD_SUBJECT = "Password reset for " + WebProtegeProperties.get().getApplicationName();

    public static final String RESET_PASSWORD = "Wel2010come";
    public static final String FORGOT_PASSWORD_EMAIL_BODY =
        "Your password has been reset to: " + RESET_PASSWORD +
           "\n\nPlease change your password from the Options menu the next time you log into " +
              WebProtegeProperties.get().getApplicationName()+ "." +
               "\n\n The " + WebProtegeProperties.get().getApplicationName() + " administrator" ;

    public static final String INVITATION_SUBJECT = WebProtegeProperties.get().getApplicationName()  + " User Acccount Invitation";

    public static final String INVITATION_BODY = "'<AuthorName>' has invited you as a '<AccessType>' for " +
            WebProtegeProperties.get().getApplicationName() +" project '<ProjectName>' . \n\n" +
            " To accept the invitation click the following link \n\n" +
            "<InvitationLink>";

}
