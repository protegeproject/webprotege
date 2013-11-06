package edu.stanford.bmir.protege.web.server.mail;

import javax.mail.MessagingException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/11/2013
 */
public class ConsoleMessagingExceptionHandler implements MessagingExceptionHandler {

    @Override
    public void handleMessagingException(MessagingException e) {
        e.printStackTrace();
    }
}
