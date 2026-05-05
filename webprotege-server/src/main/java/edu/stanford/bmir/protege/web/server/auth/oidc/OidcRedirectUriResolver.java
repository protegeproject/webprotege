package edu.stanford.bmir.protege.web.server.auth.oidc;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;

/**
 * Builds the public callback URL registered with the OIDC client (Keycloak).
 */
public final class OidcRedirectUriResolver {

    private OidcRedirectUriResolver() {
    }

    @Nonnull
    public static String callbackUrl(@Nonnull HttpServletRequest req, @Nonnull OidcRuntimeConfig config) {
        if (config.getRedirectUriOverride().isPresent()) {
            return config.getRedirectUriOverride().get();
        }
        String scheme = firstNonBlank(req.getHeader("X-Forwarded-Proto"), req.getScheme());
        String host = firstNonBlank(req.getHeader("X-Forwarded-Host"), req.getHeader("Host"));
        if (host == null || host.isEmpty()) {
            int port = req.getServerPort();
            String defaultPort = "http".equalsIgnoreCase(scheme) ? "80" : "443";
            host = req.getServerName() + (String.valueOf(port).equals(defaultPort) ? "" : ":" + port);
        }
        String context = req.getContextPath() == null ? "" : req.getContextPath();
        return scheme + "://" + host + context + "/webprotege/oidc/callback";
    }

    private static String firstNonBlank(String a, String b) {
        if (a != null && !a.trim().isEmpty()) {
            return a.trim();
        }
        return b == null ? "" : b.trim();
    }
}
