package edu.stanford.bmir.protege.web.server.auth.oidc;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Base64;

/**
 * Builds the Keycloak/OIDC authorization redirect and completes the code exchange.
 */
public class OidcAuthCoordinator {

    private static final Logger logger = LoggerFactory.getLogger(OidcAuthCoordinator.class);

    public static final String SESSION_STATE = "webprotege.oidc.state";

    public static final String SESSION_NONCE = "webprotege.oidc.nonce";

    public static final String SESSION_REDIRECT_URI = "webprotege.oidc.redirect_uri";

    private static final SecureRandom RANDOM = new SecureRandom();

    private final OidcRuntimeConfig config;

    private final OidcDiscoveryCache discoveryCache;

    private final OidcHttpTokenClient tokenClient;

    private final OidcSsoUserProvisioner userProvisioner;

    @Inject
    public OidcAuthCoordinator(@Nonnull OidcRuntimeConfig config,
                               @Nonnull OidcDiscoveryCache discoveryCache,
                               @Nonnull OidcHttpTokenClient tokenClient,
                               @Nonnull OidcSsoUserProvisioner userProvisioner) {
        this.config = config;
        this.discoveryCache = discoveryCache;
        this.tokenClient = tokenClient;
        this.userProvisioner = userProvisioner;
    }

    public boolean isEnabled() {
        return config.isEnabled();
    }

    @Nonnull
    public String buildAuthorizationRedirectUrl(@Nonnull HttpSession session,
                                                @Nonnull String redirectUriForCallback) throws IOException {
        OidcDiscoveryDocument discovery = discoveryCache.get(config.getIssuerUri());
        String state = randomUrlSafe(24);
        String nonce = randomUrlSafe(24);
        session.setAttribute(SESSION_STATE, state);
        session.setAttribute(SESSION_NONCE, nonce);
        session.setAttribute(SESSION_REDIRECT_URI, redirectUriForCallback);
        String scope = URLEncoder.encode(config.getScopes(), StandardCharsets.UTF_8);
        String redirectEnc = URLEncoder.encode(redirectUriForCallback, StandardCharsets.UTF_8);
        return discovery.getAuthorizationEndpoint()
                + "?response_type=code"
                + "&client_id=" + URLEncoder.encode(config.getClientId(), StandardCharsets.UTF_8)
                + "&redirect_uri=" + redirectEnc
                + "&scope=" + scope
                + "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8)
                + "&nonce=" + URLEncoder.encode(nonce, StandardCharsets.UTF_8);
    }

    @Nonnull
    public UserId completeLogin(@Nonnull HttpSession session,
                                @Nonnull String code,
                                @Nonnull String state) throws IOException, ParseException, BadJOSEException, JOSEException {
        Object expectedState = session.getAttribute(SESSION_STATE);
        Object expectedNonce = session.getAttribute(SESSION_NONCE);
        Object redirectUriObj = session.getAttribute(SESSION_REDIRECT_URI);
        if (!(expectedState instanceof String) || !(expectedNonce instanceof String) || !(redirectUriObj instanceof String)) {
            throw new IOException("Missing OIDC session state; restart sign-in from WebProtege.");
        }
        if (!((String) expectedState).equals(state)) {
            throw new IOException("Invalid OIDC state parameter");
        }
        String redirectUri = (String) redirectUriObj;
        String nonce = (String) expectedNonce;
        OidcDiscoveryDocument discovery = discoveryCache.get(config.getIssuerUri());
        OidcTokenResponse tokenResponse = tokenClient.exchangeAuthorizationCode(
                discovery.getTokenEndpoint(),
                code,
                redirectUri,
                config.getClientId(),
                config.getClientSecret());
        JWTClaimsSet claims = OidcIdTokenValidator.validateAndParseClaims(
                tokenResponse.getIdToken(),
                discovery.getIssuer(),
                discovery.getJwksUri(),
                config.getClientId());
        String tokenNonce = claims.getStringClaim("nonce");
        if (tokenNonce == null || !tokenNonce.equals(nonce)) {
            throw new IOException("Invalid OIDC nonce in ID token");
        }
        UserId userId = userProvisioner.resolveOrProvisionUser(claims, config.getUsernameClaim());
        session.removeAttribute(SESSION_STATE);
        session.removeAttribute(SESSION_NONCE);
        session.removeAttribute(SESSION_REDIRECT_URI);
        logger.info("OIDC login successful for WebProtege user {}", userId.getUserName());
        return userId;
    }

    @Nonnull
    private static String randomUrlSafe(int numBytes) {
        byte[] b = new byte[numBytes];
        RANDOM.nextBytes(b);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
    }
}
