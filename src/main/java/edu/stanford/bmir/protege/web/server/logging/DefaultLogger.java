package edu.stanford.bmir.protege.web.server.logging;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.SerializationException;
import edu.stanford.bmir.protege.web.server.inject.AdminEmail;
import edu.stanford.bmir.protege.web.server.mail.SendMail;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import edu.stanford.bmir.protege.web.shared.permissions.GroupId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.IllegalFormatException;
import java.util.Set;
import java.util.logging.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/03/2013
 */
public class DefaultLogger implements WebProtegeLogger {

    private static final String SUBJECT = "Unexpected Exception in webprotege";

    public static final String LOG_NAME = "webprotege";

    private Logger logger;

    private Provider<Optional<String>> emailAddressProvider;

    private SendMail sendMail;

    @Inject
    public DefaultLogger(@AdminEmail Provider<Optional<String>> emailAddressProvider, SendMail sendMail) {
        this.logger = Logger.getLogger(LOG_NAME);
        this.emailAddressProvider = emailAddressProvider;
        this.sendMail = sendMail;
        if (logger.getUseParentHandlers()) {
            this.logger.setUseParentHandlers(false);
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new WebProtegeLogFormatter());
            this.logger.addHandler(consoleHandler);
        }
    }

    @Override
    public void severe(Throwable t, UserId userId) {
        String message = formatMessage(t, Optional.of(userId), Optional.<HttpServletRequest>absent());
        logSevereMessage(message, isMailableException(t));
    }

    @Override
    public void severe(Throwable t, UserId userId, HttpServletRequest servletRequest) {
        String message = formatMessage(t, Optional.of(userId), Optional.of(servletRequest));
        logSevereMessage(message, isMailableException(t));
    }

    @Override
    public void severe(Throwable t) {
        String message = formatMessage(t, Optional.<UserId>absent(), Optional.<HttpServletRequest>absent());
        logSevereMessage(message, isMailableException(t));
    }

    private void logSevereMessage(String message, boolean sendMail) {
        if (sendMail) {
            emailMessage(message);
        }
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
                Set<GroupId> groups = getMetaProjectManager().getUserGroups(id);
                Optional<String> email = getMetaProjectManager().getEmail(id);
                pw.println("email: " + email.or("Not known"));
                pw.println("groups: ");
                for(GroupId group : groups) {
                    pw.println("        " + group.getGroupName());
                }
            }
        }
        if(request.isPresent()) {
            HttpServletRequest req = request.get();
            pw.println("Request URI: " + req.getRequestURI());
            String remoteAddr = req.getRemoteAddr();
            if (remoteAddr != null) {
                pw.println("Remote address: " + remoteAddr);
            }
            String remoteHost = req.getRemoteHost();
            if (remoteHost != null) {
                pw.println("Remote host: " + remoteHost);
            }
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

    private MetaProjectManager getMetaProjectManager() {
        return MetaProjectManager.getManager();
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

    @Override
    public void info(ProjectId projectId, String message, Object... args) {
        if(!logger.isLoggable(Level.INFO)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Project: ");
        sb.append(projectId.getId());
        sb.append("\n");
        sb.append(formatMessage(message, args));
        writeToLog(sb.toString(), Level.INFO);
    }

    private String formatMessage(String message, Object[] args) {
        String formattedMessage;
        try {
            formattedMessage = String.format(message, args);
        }
        catch (IllegalFormatException e) {
            formattedMessage = "Illegally formatted log message: " + message + ". " + e.getMessage();
        }
        return formattedMessage;
    }


    private void emailMessage(String message) {
        try {
            Optional<String> emailAddress = emailAddressProvider.get();
            if (emailAddress.isPresent()) {
                sendMail.sendMail(emailAddress.get(), SUBJECT, message);
            }
        }
        catch (Throwable e) {
            info("Problem sending mail %s", e.getMessage());
        }
    }

    private void writeToLog(String message, Level level) {
        if (logger.isLoggable(level)) {
            logger.log(level, message);
        }
    }

    private boolean isMailableException(Throwable throwable) {
        // We get too many of these on deployment sometimes.  For now, exclude from emailing.
        return !(throwable instanceof SerializationException);
    }
}
