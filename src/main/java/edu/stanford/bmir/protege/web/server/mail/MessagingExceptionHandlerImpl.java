package edu.stanford.bmir.protege.web.server.mail;

import javax.inject.Inject;
import javax.mail.MessagingException;
import java.util.logging.Logger;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/11/2013
 */
public class MessagingExceptionHandlerImpl implements MessagingExceptionHandler {

    private final Logger logger;

    @Inject
    public MessagingExceptionHandlerImpl() {
        logger = Logger.getLogger("MessagingException");
    }

    @Override
    public void handleMessagingException(MessagingException e) {
        // We don't log this as error because logging error messages sends an email to the admin - we'd go round
        // in an endless loop!
        logger.info("WARNING: Could not send email. " + e.getMessage());
    }
}
