package edu.stanford.bmir.protege.web.server.mail;

import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;

import javax.mail.MessagingException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/11/2013
 */
public class WebProtegeLoggerMessagingExceptionHandler implements MessagingExceptionHandler {

    public static final WebProtegeLogger LOGGER = WebProtegeLoggerManager.get(WebProtegeLoggerMessagingExceptionHandler.class);

    @Override
    public void handleMessagingException(MessagingException e) {
        // We don't log this as severe because logging severe messages sends an email to the admin - we'd go round
        // in an endless loop!
        LOGGER.info("WARNING: Could not send email.  %s", e.getMessage());
    }
}
