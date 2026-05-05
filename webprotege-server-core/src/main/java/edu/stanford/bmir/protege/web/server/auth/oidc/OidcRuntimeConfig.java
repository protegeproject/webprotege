package edu.stanford.bmir.protege.web.server.auth.oidc;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Resolved OpenID Connect settings for Keycloak (or any OIDC provider). See
 * {@link OidcRuntimeConfigLoader} for the supported configuration keys.
 */
public final class OidcRuntimeConfig {

    public static final OidcRuntimeConfig DISABLED = new OidcRuntimeConfig(false, "", "", "", Optional.empty(),
            "openid profile email", "preferred_username", false);

    private final boolean enabled;

    @Nonnull
    private final String issuerUri;

    @Nonnull
    private final String clientId;

    @Nonnull
    private final String clientSecret;

    @Nonnull
    private final Optional<String> redirectUriOverride;

    @Nonnull
    private final String scopes;

    @Nonnull
    private final String usernameClaim;

    private final boolean hideLocalLogin;

    private OidcRuntimeConfig(boolean enabled,
                              @Nonnull String issuerUri,
                              @Nonnull String clientId,
                              @Nonnull String clientSecret,
                              @Nonnull Optional<String> redirectUriOverride,
                              @Nonnull String scopes,
                              @Nonnull String usernameClaim,
                              boolean hideLocalLogin) {
        this.enabled = enabled;
        this.issuerUri = issuerUri;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUriOverride = redirectUriOverride;
        this.scopes = scopes;
        this.usernameClaim = usernameClaim;
        this.hideLocalLogin = hideLocalLogin;
    }

    @Nonnull
    public static OidcRuntimeConfig enabled(@Nonnull String issuerUri,
                                            @Nonnull String clientId,
                                            @Nonnull String clientSecret,
                                            @Nonnull Optional<String> redirectUriOverride,
                                            @Nonnull String scopes,
                                            @Nonnull String usernameClaim,
                                            boolean hideLocalLogin) {
        return new OidcRuntimeConfig(true,
                normalizeIssuer(checkNotNull(issuerUri)),
                checkNotNull(clientId),
                checkNotNull(clientSecret),
                checkNotNull(redirectUriOverride),
                scopes.isEmpty() ? "openid profile email" : scopes,
                usernameClaim.isEmpty() ? "preferred_username" : usernameClaim,
                hideLocalLogin);
    }

    @Nonnull
    private static String normalizeIssuer(@Nonnull String issuer) {
        String trimmed = issuer.trim();
        while (trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Nonnull
    public String getIssuerUri() {
        return issuerUri;
    }

    @Nonnull
    public String getClientId() {
        return clientId;
    }

    @Nonnull
    public String getClientSecret() {
        return clientSecret;
    }

    @Nonnull
    public Optional<String> getRedirectUriOverride() {
        return redirectUriOverride;
    }

    @Nonnull
    public String getScopes() {
        return scopes;
    }

    @Nonnull
    public String getUsernameClaim() {
        return usernameClaim;
    }

    public boolean isHideLocalLogin() {
        return hideLocalLogin;
    }
}
