package edu.stanford.bmir.protege.web.server.logging;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.IllegalFormatException;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/03/2013
 */
public class DefaultLogger implements WebProtegeLogger {

    private static final Logger logger = LoggerFactory.getLogger(DefaultLogger.class);

    @Inject
    public DefaultLogger() {
    }

    @Override
    public void error(Throwable t, UserId userId) {
        String message = formatMessage(t, Optional.of(userId), Optional.empty());
        logErrorMessage(message);
    }

    @Override
    public void error(Throwable t, UserId userId, HttpServletRequest servletRequest) {
        String message = formatMessage(t, Optional.of(userId), Optional.of(servletRequest));
        logErrorMessage(message);
    }

    @Override
    public void error(Throwable t) {
        String message = formatMessage(t, Optional.empty(), Optional.empty());
        logErrorMessage(message);
    }

    private void logErrorMessage(String message) {
        logger.error(WebProtegeMarker, message);
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

    @Override
    public void info(String message) {
        logger.info(WebProtegeMarker, message);
    }



    @Override
    public void info(String message, Object... args) {
        if(!logger.isInfoEnabled()) {
            return;
        }
        String formattedMessage = formatMessage(message, args);
        logger.info(WebProtegeMarker, formattedMessage);
    }

    @Override
    public void info(ProjectId projectId, String message, Object... args) {
        if(!logger.isInfoEnabled()) {
            return;
        }
        String formattedMessage = String.format("ProjectId: %s    %s", projectId.getId(), formatMessage(message, args));
        logger.info(WebProtegeMarker, formattedMessage);
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
}
