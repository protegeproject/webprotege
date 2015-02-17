package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class GetChapDataAction implements Action<GetChapDataResult> {

    private UserId userId;

    /**
     * For serialization purposes only
     */
    private GetChapDataAction() {
    }

    public GetChapDataAction(UserId userId) {
        this.userId = checkNotNull(userId);
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetChapDataAction)) {
            return false;
        }
        GetChapDataAction other = (GetChapDataAction) obj;
        return this.userId.equals(other.userId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetChapDataAction")
                .addValue(userId)
                .toString();
    }
}
