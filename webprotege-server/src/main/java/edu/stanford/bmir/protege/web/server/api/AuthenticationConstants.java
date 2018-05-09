package edu.stanford.bmir.protege.web.server.api;

import edu.stanford.bmir.protege.web.shared.api.ApiKey;
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

    /**
     * This property is used to set the {@link ApiKey} associated with a caller
     * in the {@link ContainerRequestContext}.  This property is propagated through to
     * {@link HttpServletRequest} objects when running in a servlet container.  Note
     * that the value of this property may be null if an Authorized header has not
     * been set, or if an invalid API key has been supplied in the Authorized header.
     */
    public static final String AUTHENTICATED_USER_API_KEY = "auth.apiKey";

}
