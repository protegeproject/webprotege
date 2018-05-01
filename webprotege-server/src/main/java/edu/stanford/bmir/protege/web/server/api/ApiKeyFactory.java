package edu.stanford.bmir.protege.web.server.api;

import edu.stanford.bmir.protege.web.shared.api.ApiKey;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.glassfish.hk2.api.Factory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.api.AuthenticationConstants.AUTHENTICATED_USER_API_KEY;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 May 2018
 *
 * A factory that creates API
 */
public class ApiKeyFactory implements Factory<ApiKey> {


    @Context
    private HttpServletRequest request;

    @Inject
    public ApiKeyFactory(@Nonnull HttpServletRequest request) {
        this.request = checkNotNull(request);
    }

    @Nullable
    @Override
    public ApiKey provide() {
        // Provide the api key from the AUTHENTICATED_USER_API_KEY attribute that may have
        // been set by the AuthenticationFilter if the Authorization header is present
        return  (ApiKey) request.getAttribute(AUTHENTICATED_USER_API_KEY);
    }

    @Override
    public void dispose(ApiKey userId) {

    }
}
