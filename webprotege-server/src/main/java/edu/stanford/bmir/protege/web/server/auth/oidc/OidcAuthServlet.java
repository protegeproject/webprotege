package edu.stanford.bmir.protege.web.server.auth.oidc;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSessionImpl;
import edu.stanford.bmir.protege.web.server.user.UserActivityManager;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

/**
 * OIDC authorization code flow entry points for Keycloak (or compatible IdPs).
 * <ul>
 *     <li>{@code GET /webprotege/oidc/login} — redirects the browser to the IdP</li>
 *     <li>{@code GET /webprotege/oidc/callback} — handles the redirect back, establishes the session</li>
 * </ul>
 */
@ApplicationSingleton
public class OidcAuthServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(OidcAuthServlet.class);

    @Nonnull
    private final OidcRuntimeConfig config;

    @Nonnull
    private final OidcAuthCoordinator coordinator;

    @Nonnull
    private final UserActivityManager activityManager;

    @Inject
    public OidcAuthServlet(@Nonnull OidcRuntimeConfig config,
                           @Nonnull OidcAuthCoordinator coordinator,
                           @Nonnull UserActivityManager activityManager) {
        this.config = config;
        this.coordinator = coordinator;
        this.activityManager = activityManager;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!config.isEnabled() || !coordinator.isEnabled()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String servletPath = req.getServletPath();
        if (servletPath.endsWith("/login")) {
            handleLogin(req, resp);
        }
        else if (servletPath.endsWith("/callback")) {
            handleCallback(req, resp);
        }
        else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String callbackUrl = OidcRedirectUriResolver.callbackUrl(req, config);
        try {
            String location = coordinator.buildAuthorizationRedirectUrl(req.getSession(), callbackUrl);
            resp.sendRedirect(location);
        } catch (Exception e) {
            logger.error("OIDC login redirect failed: {}", e.getMessage(), e);
            resp.sendRedirect(errorLanding(req, "start"));
        }
    }

    private void handleCallback(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter("code");
        String state = req.getParameter("state");
        String error = req.getParameter("error");
        if (error != null) {
            logger.warn("OIDC provider returned error: {} / {}", error, req.getParameter("error_description"));
            resp.sendRedirect(errorLanding(req, "provider"));
            return;
        }
        if (code == null || state == null) {
            resp.sendRedirect(errorLanding(req, "missing"));
            return;
        }
        try {
            UserId userId = coordinator.completeLogin(req.getSession(), code, state);
            WebProtegeSession webSession = new WebProtegeSessionImpl(req.getSession());
            webSession.setUserInSession(userId);
            activityManager.setLastLogin(userId, System.currentTimeMillis());
            resp.sendRedirect(successLanding(req));
        } catch (IOException | ParseException | BadJOSEException | JOSEException e) {
            logger.error("OIDC callback failed: {}", e.getMessage(), e);
            resp.sendRedirect(errorLanding(req, "token"));
        }
    }

    @Nonnull
    private static String successLanding(HttpServletRequest req) {
        return req.getContextPath() + "/WebProtege.jsp";
    }

    @Nonnull
    private static String errorLanding(HttpServletRequest req, String code) {
        return req.getContextPath() + "/WebProtege.jsp?oidc_error=" + code;
    }
}
