package edu.stanford.bmir.protege.web.server.mail;

import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;

import javax.inject.Inject;
import javax.mail.MessagingException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/11/2013
 */
public class WebProtegeLoggerMessagingExceptionHandler implements MessagingExceptionHandler {

    private final WebProtegeLogger logger;

    @Inject
    public WebProtegeLoggerMessagingExceptionHandler(WebProtegeLogger logger) {
        this.logger = logger;
    }

    @Override
    public void handleMessagingException(MessagingException e) {
        // We don't log this as severe because logging severe messages sends an email to the admin - we'd go round
        // in an endless loop!
        logger.info("WARNING: Could not send email.  %s", e.getMessage());
    }
}
