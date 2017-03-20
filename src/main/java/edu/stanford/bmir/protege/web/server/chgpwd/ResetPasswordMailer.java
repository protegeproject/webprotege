package edu.stanford.bmir.protege.web.server.chgpwd;

import edu.stanford.bmir.protege.web.server.filemanager.FileContentsCache;
import edu.stanford.bmir.protege.web.server.filemanager.HasGetFile;
import edu.stanford.bmir.protege.web.server.inject.OverridableFile;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.mail.MailManager;
import edu.stanford.bmir.protege.web.server.place.PlaceUrl;
import edu.stanford.bmir.protege.web.server.templates.TemplateEngine;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonList;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
public class ResetPasswordMailer {

    public static final String SUBJECT = "Your password has been reset";

    private final MailManager mailManager;

    private final WebProtegeLogger logger;

    private final TemplateEngine templateEngine;

    private final FileContentsCache templateFile;

    private final PlaceUrl placeUrl;

    @Inject
    public ResetPasswordMailer(MailManager mailManager,
                               TemplateEngine templateEngine,
                               @PasswordResetEmailTemplate FileContentsCache templateFile,
                               PlaceUrl placeUrl,
                               WebProtegeLogger logger) {
        this.mailManager = mailManager;
        this.templateEngine = templateEngine;
        this.templateFile = templateFile;
        this.placeUrl = placeUrl;
        this.logger = logger;
    }

    public void sendEmail(final UserId userId, final String emailAddress, final String pwd) {
        try {
            Map<String, Object> objects = new HashMap<>();
            objects.put("pwd", pwd);
            objects.put("userId", userId);
            objects.put("application.url", placeUrl.getApplicationUrl());

            String template = templateFile.getContents();
            String emailBody = templateEngine.populateTemplate(template, objects);

            mailManager.sendMail(singletonList(emailAddress), SUBJECT, emailBody, e -> {
                logger.info("A password reset email could not be sent to user % at %s.  The password was reset to %s.",
                        userId.getUserName(),
                        emailAddress,
                        pwd);
            });
        } catch (IOException e) {
            logger.info("A problem occurred when populating the password reset email template: {}", e.getMessage());
        }
    }
}
