package edu.stanford.bmir.protege.web.shared.user;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
public class CreateUserAccountAction implements Action<CreateUserAccountResult> {

    private UserId userId;

    private EmailAddress emailAddress;

    private SaltedPasswordDigest passwordDigest;

    private Salt salt;

    /**
     * For Serialization purposes only
     */
    private CreateUserAccountAction() {
    }

    public CreateUserAccountAction(UserId userId, EmailAddress emailAddress, SaltedPasswordDigest passwordDigest, Salt salt) {
        this.userId = checkNotNull(userId);
        this.emailAddress = checkNotNull(emailAddress);
        this.passwordDigest = checkNotNull(passwordDigest);
        this.salt = checkNotNull(salt);
    }

    public UserId getUserId() {
        return userId;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public SaltedPasswordDigest getPasswordDigest() {
        return passwordDigest;
    }

    public Salt getSalt() {
        return salt;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId, emailAddress, passwordDigest, salt);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CreateUserAccountAction)) {
            return false;
        }
        CreateUserAccountAction other = (CreateUserAccountAction) obj;
        return this.getUserId().equals(other.getUserId())
                && this.getEmailAddress().equals(other.getEmailAddress())
                && this.getPasswordDigest().equals(other.getPasswordDigest())
                && this.getSalt().equals(other.getSalt());
    }


    @Override
    public String toString() {
        return toStringHelper("CreateUserAccountAction")
                .addValue(userId)
                .addValue(emailAddress)
                .addValue(passwordDigest)
                .addValue(salt)
                .toString();
    }
}
