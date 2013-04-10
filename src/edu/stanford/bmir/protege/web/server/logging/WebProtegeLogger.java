package edu.stanford.bmir.protege.web.server.logging;

import edu.stanford.bmir.protege.web.client.rpc.data.UserId;

import javax.servlet.http.HttpServletRequest;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/03/2013
 */
public interface WebProtegeLogger {

    void severe(Throwable t, UserId userId);

    void severe(Throwable t, UserId userId, HttpServletRequest servletRequest);

    void severe(Throwable t);

    void info(String message);

    void info(String message, Object... args);

}
