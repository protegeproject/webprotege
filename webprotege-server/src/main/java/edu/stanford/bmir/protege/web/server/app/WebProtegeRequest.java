package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.keycloak.KeycloakPrincipal;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-17
 */
public class WebProtegeRequest {

    @Nonnull
    private final HttpServletRequest request;

    public WebProtegeRequest(@Nonnull HttpServletRequest request) {
        this.request = checkNotNull(request);
    }

    public UserId getUserId() {
        var userPrincipal = (KeycloakPrincipal) request.getUserPrincipal();
        var context = userPrincipal.getKeycloakSecurityContext();
        var idToken = context.getIdToken();
        return UserId.getUserId(idToken.getPreferredUsername());
    }
}
