package edu.stanford.bmir.protege.web.server.chgpwd;

import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.mail.MailManager;
import edu.stanford.bmir.protege.web.server.mail.MessagingExceptionHandler;

import javax.inject.Inject;
import javax.mail.MessagingException;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
public class ResetPasswordMailer {

    private final WebProtegeLogger logger = WebProtegeLoggerManager.get(ResetPasswordMailer.class);

    public static final String SUBJECT = "Your WebProtégé password has been reset";

    public static final String BODY_TEMPLATE = "Your WebProtégé password has been reset to:" +
            "\n\n " +
            "\t\t%s" +
            "\n\n" +
            "Please sign in using this new password and then change it after signing in.";


    private MailManager mailManager;

    @Inject
    public ResetPasswordMailer(MailManager mailManager) {
        this.mailManager = mailManager;
    }

    public void sendEmail(final String emailAddress, final String pwd) {
        mailManager.sendMail(emailAddress, SUBJECT, String.format(BODY_TEMPLATE, pwd), new MessagingExceptionHandler() {
            @Override
            public void handleMessagingException(MessagingException e) {
                logger.info("An password reset email could not be sent to %s.  The password was reset to %s.",
                            emailAddress,
                            pwd);
            }
        });
    }
}
