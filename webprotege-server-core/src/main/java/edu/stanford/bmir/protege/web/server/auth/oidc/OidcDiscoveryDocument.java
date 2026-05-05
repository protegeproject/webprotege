package edu.stanford.bmir.protege.web.server.auth.oidc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Fetches and parses OpenID Provider Metadata from {@code {issuer}/.well-known/openid-configuration}.
 */
public final class OidcDiscoveryDocument {

    private static final Logger logger = LoggerFactory.getLogger(OidcDiscoveryDocument.class);

    private final long fetchedAtMillis;

    @Nonnull
    private final String issuer;

    @Nonnull
    private final String authorizationEndpoint;

    @Nonnull
    private final String tokenEndpoint;

    @Nonnull
    private final String jwksUri;

    private OidcDiscoveryDocument(long fetchedAtMillis,
                                  @Nonnull String issuer,
                                  @Nonnull String authorizationEndpoint,
                                  @Nonnull String tokenEndpoint,
                                  @Nonnull String jwksUri) {
        this.fetchedAtMillis = fetchedAtMillis;
        this.issuer = issuer;
        this.authorizationEndpoint = authorizationEndpoint;
        this.tokenEndpoint = tokenEndpoint;
        this.jwksUri = jwksUri;
    }

    public boolean isStale(long maxAgeMillis) {
        return System.currentTimeMillis() - fetchedAtMillis > maxAgeMillis;
    }

    @Nonnull
    public String getIssuer() {
        return issuer;
    }

    @Nonnull
    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    @Nonnull
    public String getTokenEndpoint() {
        return tokenEndpoint;
    }

    @Nonnull
    public String getJwksUri() {
        return jwksUri;
    }

    @Nonnull
    public static OidcDiscoveryDocument fetch(@Nonnull String issuerUri, @Nonnull ObjectMapper objectMapper) throws IOException {
        String wellKnown = issuerUri + "/.well-known/openid-configuration";
        logger.debug("Fetching OIDC discovery document from {}", wellKnown);
        HttpGet get = new HttpGet(wellKnown);
        try (CloseableHttpClient client = HttpClients.createSystem();
             CloseableHttpResponse response = client.execute(get)) {
            int code = response.getStatusLine().getStatusCode();
            if (code < 200 || code >= 300) {
                throw new IOException("Discovery request failed: HTTP " + code + " for " + wellKnown);
            }
            String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            JsonNode root = objectMapper.readTree(body);
            String issuer = textRequired(root, "issuer");
            String authz = textRequired(root, "authorization_endpoint");
            String token = textRequired(root, "token_endpoint");
            String jwks = textRequired(root, "jwks_uri");
            return new OidcDiscoveryDocument(System.currentTimeMillis(), issuer, authz, token, jwks);
        }
    }

    @Nonnull
    private static String textRequired(JsonNode root, String field) throws IOException {
        JsonNode n = root.get(field);
        if (n == null || !n.isTextual() || n.asText().isEmpty()) {
            throw new IOException("Discovery document missing field: " + field);
        }
        return n.asText();
    }

    public static long defaultMaxCacheAgeMillis() {
        return TimeUnit.HOURS.toMillis(1);
    }

    /**
     * Validates that discovery issuer matches configured issuer (ignoring trailing slash).
     */
    public void assertIssuerCompatible(@Nonnull String configuredIssuer) throws IOException {
        String a = trimTrailingSlash(configuredIssuer);
        String b = trimTrailingSlash(issuer);
        if (!a.equals(b)) {
            throw new IOException("Discovery issuer mismatch: expected " + a + " but document has " + b);
        }
    }

    @Nonnull
    private static String trimTrailingSlash(@Nonnull String s) {
        String t = s;
        while (t.endsWith("/")) {
            t = t.substring(0, t.length() - 1);
        }
        return t;
    }
}
