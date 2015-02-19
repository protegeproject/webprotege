package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
public class AuthenticateUserAction extends AbstractAuthenticationAction<AuthenticateUserResult> {

    /**
     * For serialization purposes only
     */
    private AuthenticateUserAction() {
    }

    public AuthenticateUserAction(UserId userId, ChapSessionId chapSessionId, ChapResponse chapResponse) {
        super(userId, chapSessionId, chapResponse);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserId(), getChapSessionId(), getChapResponse());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AuthenticateUserAction)) {
            return false;
        }
        AuthenticateUserAction other = (AuthenticateUserAction) obj;
        return this.getUserId().equals(other.getUserId())
                && this.getChapSessionId().equals(other.getChapSessionId())
                && this.getChapResponse().equals(other.getChapResponse());
    }


    @Override
    public String toString() {
        return toStringHelper("AuthenticateUserAction")
                .addValue(getUserId())
                .addValue(getChapSessionId())
                .addValue(getChapResponse())
                .toString();
    }
}
