package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class PerformLoginResult extends AbstractAuthenticationResult {

    private UserInSession userInSession;

    public PerformLoginResult(@Nonnull AuthenticationResponse result,
                              @Nonnull UserInSession userInSession) {
        super(result);
        this.userInSession = checkNotNull(userInSession);
    }

    @GwtSerializationConstructor
    private PerformLoginResult() {
    }

    /**
     * Gets the user details of the user after the attempted login.  If authentication failed then the guest
     * details will be returned.
     */
    @Nonnull
    public UserDetails getUserDetails() {
        return userInSession.getUserDetails();
    }

    @Nonnull
    public UserInSession getUserInSession() {
        return userInSession;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getResponse(), userInSession);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PerformLoginResult)) {
            return false;
        }
        PerformLoginResult other = (PerformLoginResult) obj;
        return this.getResponse() == other.getResponse() && this.userInSession.equals(other.userInSession);
    }


    @Override
    public String toString() {
        return toStringHelper("PerformLoginResult")
                .addValue(getResponse())
                .addValue(userInSession)
                .toString();
    }
}
