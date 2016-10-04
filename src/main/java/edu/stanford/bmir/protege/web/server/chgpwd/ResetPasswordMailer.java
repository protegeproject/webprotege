package edu.stanford.bmir.protege.web.server.chgpwd;

import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.mail.MailManager;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
public class ResetPasswordMailer {

    public static final String SUBJECT = "Your WebProtégé password has been reset";

    public static final String BODY_TEMPLATE = "Your WebProtégé password (for user name %s) has been reset to:" +
            "\n\n " +
            "\t\t%s" +
            "\n\n" +
            "Please sign in using this new password and then change it after signing in.";


    private final MailManager mailManager;

    private final WebProtegeLogger logger;

    @Inject
    public ResetPasswordMailer(MailManager mailManager, WebProtegeLogger logger) {
        this.mailManager = mailManager;
        this.logger = logger;
    }

    public void sendEmail(final UserId userId, final String emailAddress, final String pwd) {
        mailManager.sendMail(emailAddress, SUBJECT, String.format(BODY_TEMPLATE, userId.getUserName(), pwd), e -> {
            logger.info("A password reset email could not be sent to user % at %s.  The password was reset to %s.",
                    userId.getUserName(),
                    emailAddress,
                    pwd);
        });
    }
}
