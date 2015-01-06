package edu.stanford.bmir.protege.web.shared.user;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/01/15
 */
public class GetUserIdsResult implements Result {

    private ImmutableList<UserId> userIds;

    private GetUserIdsResult() {
    }

    public GetUserIdsResult(ImmutableList<UserId> userIds) {
        this.userIds = checkNotNull(userIds);
    }

    public ImmutableList<UserId> getUserIds() {
        return userIds;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userIds);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetUserIdsResult)) {
            return false;
        }
        GetUserIdsResult other = (GetUserIdsResult) obj;
        return this.userIds.equals(other.userIds);
    }


    @Override
    public String toString() {
        return toStringHelper("GetUserIdsResult")
                .addValue(userIds)
                .toString();
    }
}
