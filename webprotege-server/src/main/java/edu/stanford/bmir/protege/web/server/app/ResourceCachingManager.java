package edu.stanford.bmir.protege.web.server.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/01/15
 */
public class ResourceCachingManager {

    private static final String EXPIRES_ACTIVE = "ExpiresActive";

    private static final String EXPIRES_ACTIVE_ON = "on";

    private static final String EXPIRES_DEFAULT = "ExpiresDefault";

    private static final String EXPIRES_DEFAULT_VALUE = "now plus 1 year";

    private final ResourceCachingStrategy resourceCachingStrategy;

    public ResourceCachingManager(ResourceCachingStrategy resourceCachingStrategy) {
        this.resourceCachingStrategy = resourceCachingStrategy;
    }

    public void setAppropriateResponseHeaders(HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        if(resourceCachingStrategy.shouldCacheResource(requestURI)) {
            response.setHeader(EXPIRES_ACTIVE, EXPIRES_ACTIVE_ON);
            response.setHeader(EXPIRES_DEFAULT, EXPIRES_DEFAULT_VALUE);
        }
    }
}
