package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.server.mail.MailManager;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/11/2013
 * <p>
 *     A singleton instance that manages per app functionality on the server side.
 * </p>
 */
public class App {

    private static final App instance = new App();

    private MailManager mailManager;

    private App() {
    }

    public static App get() {
        return instance;
    }

    public void setMailManager(MailManager mailManager) {
        this.mailManager = mailManager;
    }

    public MailManager getMailManager() {
        if(mailManager == null) {
            throw new IllegalStateException("App has not be initialized properly.  No MailManager has been set.");
        }
        return mailManager;
    }
}
