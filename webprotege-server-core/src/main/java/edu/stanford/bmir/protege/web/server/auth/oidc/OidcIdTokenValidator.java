package edu.stanford.bmir.protege.web.server.auth.oidc;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

/**
 * Validates an OIDC ID token signature and core claims against the provider JWKS.
 */
public final class OidcIdTokenValidator {

    private OidcIdTokenValidator() {
    }

    @Nonnull
    public static JWTClaimsSet validateAndParseClaims(@Nonnull String idTokenString,
                                                      @Nonnull String expectedIssuer,
                                                      @Nonnull String jwksUri,
                                                      @Nonnull String clientId) throws ParseException, BadJOSEException, JOSEException, MalformedURLException {
        SignedJWT signedJWT = SignedJWT.parse(idTokenString);
        JWSAlgorithm alg = signedJWT.getHeader().getAlgorithm();
        if (alg == null) {
            throw new JOSEException("Missing JWS algorithm in ID token header");
        }
        URL jwksUrl = new URL(jwksUri);
        JWKSource<SecurityContext> keySource = new RemoteJWKSet<>(jwksUrl);
        ConfigurableJWTProcessor<SecurityContext> processor = new DefaultJWTProcessor<>();
        JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(alg, keySource);
        processor.setJWSKeySelector(keySelector);
        JWTClaimsSet claims = processor.process(signedJWT, null);
        if (!expectedIssuer.equals(claims.getIssuer())) {
            throw new BadJOSEException("ID token issuer mismatch");
        }
        if (!audienceAcceptsClient(claims, clientId)) {
            throw new BadJOSEException("ID token audience mismatch for client_id=" + clientId);
        }
        return claims;
    }

    private static boolean audienceAcceptsClient(@Nonnull JWTClaimsSet claims, @Nonnull String clientId) throws ParseException {
        List<String> aud = claims.getAudience();
        if (aud != null) {
            for (String a : aud) {
                if (clientId.equals(a)) {
                    return true;
                }
            }
        }
        String azp = claims.getStringClaim("azp");
        return clientId.equals(azp);
    }
}
