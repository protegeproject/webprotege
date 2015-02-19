package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
public class AuthenticateUserResult extends AbstractAuthenticationResult {

    /**
     * For serialization purposes only
     */
    private AuthenticateUserResult() {
    }

    public AuthenticateUserResult(AuthenticationResponse response) {
        super(response);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getResponse());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AuthenticateUserResult)) {
            return false;
        }
        AuthenticateUserResult other = (AuthenticateUserResult) obj;
        return this.getResponse() == other.getResponse();
    }


    @Override
    public String toString() {
        return toStringHelper("AuthenticateUserResult")
                .addValue(getResponse())
                .toString();
    }
}
