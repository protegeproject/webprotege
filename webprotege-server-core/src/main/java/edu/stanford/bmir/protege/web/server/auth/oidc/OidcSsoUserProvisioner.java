package edu.stanford.bmir.protege.web.server.auth.oidc;

import com.google.common.io.BaseEncoding;
import com.nimbusds.jwt.JWTClaimsSet;
import edu.stanford.bmir.protege.web.server.auth.AuthenticationManager;
import edu.stanford.bmir.protege.web.server.user.UserRecord;
import edu.stanford.bmir.protege.web.server.user.UserRecordRepository;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserEmailAlreadyExistsException;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.user.UserRegistrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Maps OIDC claims to a {@link UserId}, optionally creating a local account with an unusable password digest
 * (SSO-only users cannot complete the CHAP password login path).
 */
public class OidcSsoUserProvisioner {

    private static final Logger logger = LoggerFactory.getLogger(OidcSsoUserProvisioner.class);

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserRecordRepository userRecordRepository;

    private final AuthenticationManager authenticationManager;

    @Inject
    public OidcSsoUserProvisioner(@Nonnull UserRecordRepository userRecordRepository,
                                @Nonnull AuthenticationManager authenticationManager) {
        this.userRecordRepository = checkNotNull(userRecordRepository);
        this.authenticationManager = checkNotNull(authenticationManager);
    }

    @Nonnull
    public UserId resolveOrProvisionUser(@Nonnull JWTClaimsSet claims,
                                         @Nonnull String usernameClaimName) throws ParseException {
        Optional<String> email = Optional.ofNullable(claims.getStringClaim("email"))
                .map(String::trim)
                .filter(s -> !s.isEmpty());
        boolean emailVerified = Boolean.TRUE.equals(claims.getBooleanClaim("email_verified"));
        String sub = Optional.ofNullable(claims.getSubject()).orElse("").trim();
        String rawUsername = claims.getStringClaim(usernameClaimName);
        if (rawUsername == null || rawUsername.trim().isEmpty()) {
            rawUsername = sub;
        }
        String candidate = sanitizeUserName(rawUsername);
        if (candidate.isEmpty()) {
            candidate = "u_" + shortHash(sub);
        }
        if ("guest".equalsIgnoreCase(candidate)) {
            candidate = "u_" + shortHash(sub);
        }

        Optional<UserRecord> byUsername = userRecordRepository.findOne(UserId.getUserId(candidate));
        if (byUsername.isPresent()) {
            logger.info("OIDC login matched existing user by username {} (sub={})", candidate, sub);
            return byUsername.get().getUserId();
        }

        return createAccountIfMissing(UserId.getUserId(candidate), email, emailVerified, sub);
    }

    @Nonnull
    private UserId createAccountIfMissing(@Nonnull UserId userId,
                                          @Nonnull Optional<String> email,
                                          boolean emailVerified,
                                          @Nonnull String subKey) {
        if (userRecordRepository.findOne(userId).isPresent()) {
            return userId;
        }
        String emailStr = (email.isPresent() && emailVerified) ? email.get() : "";
        Salt salt = randomSalt();
        SaltedPasswordDigest digest = randomDigest();
        try {
            authenticationManager.registerUser(userId, new EmailAddress(emailStr), digest, salt);
            logger.info("Provisioned WebProtege user {} from OIDC (sub={}, emailVerified={})",
                    userId.getUserName(), subKey, emailVerified);
            return userId;
        } catch (UserRegistrationException e) {
            if (e instanceof UserEmailAlreadyExistsException && !emailStr.isEmpty()) {
                UserId fallback = UserId.getUserId(userId.getUserName() + "_" + shortHash(subKey));
                try {
                    authenticationManager.registerUser(fallback, new EmailAddress(""), randomDigest(), randomSalt());
                } catch (UserRegistrationException e2) {
                    logger.error("Could not provision OIDC user after email conflict", e2);
                    throw e2;
                }
                logger.info("Provisioned WebProtege user {} from OIDC after email conflict on {} (sub={})",
                        fallback.getUserName(), emailStr, subKey);
                return fallback;
            }
            UserId fallback = UserId.getUserId(userId.getUserName() + "_" + shortHash(subKey));
            if (userRecordRepository.findOne(fallback).isPresent()) {
                return fallback;
            }
            try {
                authenticationManager.registerUser(fallback, new EmailAddress(emailStr), randomDigest(), randomSalt());
            } catch (UserRegistrationException e2) {
                logger.error("Could not provision OIDC user after conflict resolution", e2);
                throw e2;
            }
            logger.info("Provisioned WebProtege user {} from OIDC after registration conflict (sub={})", fallback.getUserName(), subKey);
            return fallback;
        }
    }

    @Nonnull
    private static Salt randomSalt() {
        byte[] b = new byte[16];
        SECURE_RANDOM.nextBytes(b);
        return new Salt(b);
    }

    @Nonnull
    private static SaltedPasswordDigest randomDigest() {
        byte[] b = new byte[16];
        SECURE_RANDOM.nextBytes(b);
        return new SaltedPasswordDigest(b);
    }

    @Nonnull
    private static String shortHash(@Nonnull String s) {
        int h = s.hashCode();
        byte[] b = new byte[]{(byte) (h >> 24), (byte) (h >> 16), (byte) (h >> 8), (byte) h};
        return BaseEncoding.base16().lowerCase().encode(b);
    }

    @Nonnull
    private static String sanitizeUserName(@Nonnull String raw) {
        String trimmed = raw.trim().toLowerCase(Locale.ROOT);
        if (trimmed.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(trimmed.length());
        for (int i = 0; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '_' || c == '-' || c == '.') {
                sb.append(c);
            }
            else {
                sb.append('_');
            }
        }
        String s = sb.toString();
        if (s.length() > 200) {
            s = s.substring(0, 200);
        }
        return s;
    }
}
