package edu.stanford.bmir.protege.web.server.user;

import com.google.common.base.Charsets;
import edu.stanford.bmir.protege.web.shared.auth.Md5DigestAlgorithmProvider;
import edu.stanford.bmir.protege.web.shared.auth.MessageDigestAlgorithm;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/03/16
 */
public class UserRecord {

    private final UserId userId;

    private final EmailAddress emailAddress;

    private final Salt salt;

    private final SaltedPasswordDigest saltedPasswordDigest;

    private final Optional<String> avatarUrl;

    public UserRecord(UserId userId,
                      EmailAddress emailAddress,
                      Optional<String> avatarUrl,
                      Salt salt, SaltedPasswordDigest saltedPasswordDigest) {
        this.userId = userId;
        this.emailAddress = emailAddress;
        this.avatarUrl = avatarUrl;
        this.salt = salt;
        this.saltedPasswordDigest = saltedPasswordDigest;
    }

    public UserId getUserId() {
        return userId;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public Optional<String> getAvatarUrl() {
        return avatarUrl;
    }

    public Salt getSalt() {
        return salt;
    }

    public SaltedPasswordDigest getSaltedPasswordDigest() {
        return saltedPasswordDigest;
    }
}
