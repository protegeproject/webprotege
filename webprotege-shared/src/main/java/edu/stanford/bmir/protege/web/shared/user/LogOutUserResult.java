package edu.stanford.bmir.protege.web.shared.user;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/15
 */
public class LogOutUserResult implements Result {

    private UserInSession userInSession;

    public LogOutUserResult(@Nonnull UserInSession userInSession) {
        this.userInSession = checkNotNull(userInSession);
    }

    @GwtSerializationConstructor
    private LogOutUserResult() {
    }

    @Nonnull
    public UserInSession getUserInSession() {
        return userInSession;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userInSession);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LogOutUserResult)) {
            return false;
        }
        LogOutUserResult other = (LogOutUserResult) obj;
        return this.userInSession.equals(other.userInSession);
    }


    @Override
    public String toString() {
        return toStringHelper("LogOutUserResult")
                .toString();
    }
}
