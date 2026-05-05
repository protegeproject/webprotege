package edu.stanford.bmir.protege.web.server.auth.oidc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Loads {@link OidcRuntimeConfig} from the process environment or JVM system properties.
 *
 * <p>For each setting two equivalent keys are accepted (env wins over system property):</p>
 * <table>
 *   <tr><th>Setting</th><th>Environment variable</th><th>JVM system property (-D)</th></tr>
 *   <tr><td>Issuer URI (required)</td><td>{@code WEBPROTEGE_OIDC_ISSUER_URI}</td><td>{@code webprotege.oidc.issuer.uri}</td></tr>
 *   <tr><td>Client ID (required)</td><td>{@code WEBPROTEGE_OIDC_CLIENT_ID}</td><td>{@code webprotege.oidc.client.id}</td></tr>
 *   <tr><td>Client secret (required)</td><td>{@code WEBPROTEGE_OIDC_CLIENT_SECRET}</td><td>{@code webprotege.oidc.client.secret}</td></tr>
 *   <tr><td>Redirect URI (optional)</td><td>{@code WEBPROTEGE_OIDC_REDIRECT_URI}</td><td>{@code webprotege.oidc.redirect.uri}</td></tr>
 *   <tr><td>Scopes (optional)</td><td>{@code WEBPROTEGE_OIDC_SCOPES}</td><td>{@code webprotege.oidc.scopes}</td></tr>
 *   <tr><td>Username claim (optional)</td><td>{@code WEBPROTEGE_OIDC_USERNAME_CLAIM}</td><td>{@code webprotege.oidc.username.claim}</td></tr>
 *   <tr><td>Hide local login (optional)</td><td>{@code WEBPROTEGE_OIDC_HIDE_LOCAL_LOGIN}</td><td>{@code webprotege.oidc.hide.local.login}</td></tr>
 * </table>
 */
public final class OidcRuntimeConfigLoader {

    private static final Logger logger = LoggerFactory.getLogger(OidcRuntimeConfigLoader.class);

    private OidcRuntimeConfigLoader() {
    }

    @Nonnull
    public static OidcRuntimeConfig load() {
        String issuer = read("WEBPROTEGE_OIDC_ISSUER_URI", "webprotege.oidc.issuer.uri");
        String clientId = read("WEBPROTEGE_OIDC_CLIENT_ID", "webprotege.oidc.client.id");
        String clientSecret = read("WEBPROTEGE_OIDC_CLIENT_SECRET", "webprotege.oidc.client.secret");
        if (isBlank(issuer) || isBlank(clientId) || isBlank(clientSecret)) {
            logger.info("OIDC SSO is disabled (set WEBPROTEGE_OIDC_ISSUER_URI, WEBPROTEGE_OIDC_CLIENT_ID and WEBPROTEGE_OIDC_CLIENT_SECRET to enable).");
            return OidcRuntimeConfig.DISABLED;
        }
        Optional<String> redirect = optional(read("WEBPROTEGE_OIDC_REDIRECT_URI", "webprotege.oidc.redirect.uri"));
        String scopes = nullSafeTrim(read("WEBPROTEGE_OIDC_SCOPES", "webprotege.oidc.scopes"));
        String usernameClaim = nullSafeTrim(read("WEBPROTEGE_OIDC_USERNAME_CLAIM", "webprotege.oidc.username.claim"));
        boolean hideLocal = parseBoolean(read("WEBPROTEGE_OIDC_HIDE_LOCAL_LOGIN", "webprotege.oidc.hide.local.login"));
        logger.info("OIDC SSO enabled (issuer={}, clientId={}, hideLocalLogin={})", issuer.trim(), clientId.trim(), hideLocal);
        return OidcRuntimeConfig.enabled(issuer.trim(), clientId.trim(), clientSecret.trim(), redirect, scopes, usernameClaim, hideLocal);
    }

    private static String read(String envKey, String sysPropKey) {
        String v = getenv(envKey);
        if (!isBlank(v)) {
            return v;
        }
        return getSysProp(sysPropKey);
    }

    private static Optional<String> optional(String value) {
        return isBlank(value) ? Optional.empty() : Optional.of(value.trim());
    }

    private static String nullSafeTrim(String value) {
        return value == null ? "" : value.trim();
    }

    private static boolean parseBoolean(String raw) {
        if (isBlank(raw)) {
            return false;
        }
        String t = raw.trim();
        return "true".equalsIgnoreCase(t) || "1".equals(t) || "yes".equalsIgnoreCase(t);
    }

    private static String getenv(String key) {
        try {
            return System.getenv(key);
        } catch (SecurityException e) {
            return null;
        }
    }

    private static String getSysProp(String key) {
        try {
            return System.getProperty(key, null);
        } catch (SecurityException e) {
            return null;
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
