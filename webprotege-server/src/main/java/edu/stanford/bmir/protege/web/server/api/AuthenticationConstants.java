package edu.stanford.bmir.protege.web.server.api;

import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Apr 2018
 */
public class AuthenticationConstants {

    /**
     * This property is used to set the {@link UserId} associated with a session token
     * or API key as a property/attribute in the {@link ContainerRequestContext}.  This
     * property is propagated through to {@link HttpServletRequest} objects when running
     * in a servlet container.
     */
    public static final String AUTHENTICATED_USER_ID = "auth.userId";

}
