package edu.stanford.bmir.protege.web.server.logging;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.server.ApplicationProperties;
import edu.stanford.bmir.protege.web.server.EmailUtil;
import edu.stanford.bmir.protege.web.server.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.owlapi.HierarchyProviderKey;
import edu.stanford.smi.protege.server.metaproject.Group;
import edu.stanford.smi.protege.server.metaproject.User;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.IllegalFormatException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/03/2013
 */
public class DefaultLogger implements WebProtegeLogger {

    private static final String DEFAULT_FROM_EMAIL_ADDRESS = "webprotege2012@gmail.com";

    private static final String SUBJECT = "Unexpected Exception in webprotege";


    private final String logPrefix;

    private Logger logger;

    public DefaultLogger(Class<?> cls) {
        this.logPrefix = cls.getSimpleName();
        this.logger = Logger.getLogger(cls.getName());
    }

    @Override
    public void severe(Throwable t, UserId userId) {
        String message = formatMessage(t, Optional.of(userId), Optional.<HttpServletRequest>absent());
        logSevereMessage(message);
    }

    @Override
    public void severe(Throwable t, UserId userId, HttpServletRequest servletRequest) {
        String message = formatMessage(t, Optional.of(userId), Optional.of(servletRequest));
        logSevereMessage(message);
    }

    @Override
    public void severe(Throwable t) {
        String message = formatMessage(t, Optional.<UserId>absent(), Optional.<HttpServletRequest>absent());
        logSevereMessage(message);
    }

    private void logSevereMessage(String message) {
        emailMessage(message);
        writeToLog(message, Level.SEVERE);
    }

    private String formatMessage(Throwable t, Optional<UserId> userId, Optional<HttpServletRequest> request) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println("An unexpected exception was thrown on the server");
        pw.println();
        pw.print("Date and time: ");
        pw.println(new Date());
        if (userId.isPresent()) {
            pw.println();
            pw.print("User: ");
            pw.println(userId.get().getUserName());
            final UserId id = userId.get();
            if(!id.isGuest()) {
                User user = MetaProjectManager.getManager().getMetaProject().getUser(id.getUserName());
                pw.println("email: " + user.getEmail());
                pw.println("groups: ");
                for(Group group : user.getGroups()) {
                    pw.println("        " + group.getName());
                }
            }
        }
        if(request.isPresent()) {
            HttpServletRequest req = request.get();
            pw.println("Request URI: " + req.getRequestURI());
            pw.println();
            pw.println("Headers: ");
            Enumeration headerNames = req.getHeaderNames();
            while(headerNames.hasMoreElements()) {
                Object headerName = headerNames.nextElement();
                String header = req.getHeader(headerName.toString());
                pw.print(headerName);
                pw.print(": ");
                pw.println(header);
            }
        }
        pw.println();
        pw.println();
        pw.print("Message: ");
        pw.println(t.getMessage());
        pw.println();
        pw.println("Stack trace:");
        pw.println();
        t.printStackTrace(pw);
        return sw.toString();
    }

    @Override
    public void info(String message) {
        writeToLog(message, Level.INFO);
    }



    @Override
    public void info(String message, Object... args) {
        if(!logger.isLoggable(Level.INFO)) {
            return;
        }
        String formattedMessage = formatMessage(message, args);
        writeToLog(formattedMessage, Level.INFO);
    }


    private String formatMessage(String message, Object[] args) {
        String formattedMessage;
        try {
            formattedMessage = String.format(message, args);
        }
        catch (IllegalFormatException e) {
            formattedMessage = "Illegally formatted log message: " + message;
        }
        return formattedMessage;
    }


    private void emailMessage(String message) {
        try {
            String emailAccount = ApplicationProperties.getEmailAccount();
            if(emailAccount == null || emailAccount.isEmpty()) {
                emailAccount = DEFAULT_FROM_EMAIL_ADDRESS;
            }
            EmailUtil.sendEmail(ApplicationProperties.getLoggingEmail(), SUBJECT, message, emailAccount);
        }
        catch (Throwable e) {
            info("Problem emailing message %s", e.getMessage());
        }
    }

    private void writeToLog(String message, Level level) {
        if (logger.isLoggable(level)) {
            logger.log(level, message);
        }
    }

    public static void main(String[] args) {
        DefaultLogger l = new DefaultLogger(HierarchyProviderKey.class);
        l.severe(new RuntimeException("Test throwing"));
    }
}
