package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
public class ChangePasswordAction extends AbstractAuthenticationAction<ChangePasswordResult> {

    private SaltedPasswordDigest newPassword;

    private Salt salt;

    /**
     * For serialization purposes only
     */
    private ChangePasswordAction() {
    }

    public ChangePasswordAction(UserId userId, ChapSessionId chapSessionId, ChapResponse chapResponse, SaltedPasswordDigest newPassword, Salt newSalt) {
        super(userId, chapSessionId, chapResponse);
        this.newPassword = checkNotNull(newPassword);
        this.salt = checkNotNull(newSalt);
    }

    public SaltedPasswordDigest getNewPassword() {
        return newPassword;
    }

    public Salt getNewSalt() {
        return salt;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserId(), getChapSessionId(), getChapResponse(), getNewPassword(), getNewSalt());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ChangePasswordAction)) {
            return false;
        }
        ChangePasswordAction other = (ChangePasswordAction) obj;
        return this.getUserId().equals(other.getUserId())
                && this.getChapSessionId().equals(other.getChapSessionId())
                && this.getChapResponse().equals(other.getChapResponse())
                && this.getNewPassword().equals(other.getNewPassword())
                && this.getNewSalt().equals(other.getNewSalt());
    }


    @Override
    public String toString() {
        return toStringHelper("ChangePasswordAction")
                .addValue(getUserId())
                .addValue(getChapSessionId())
                .addValue(getChapResponse())
                .addValue(getNewPassword())
                .addValue(getNewSalt())
                .toString();
    }
}
