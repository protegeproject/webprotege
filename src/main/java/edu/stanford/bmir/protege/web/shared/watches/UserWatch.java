package edu.stanford.bmir.protege.web.shared.watches;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class UserWatch<T> implements IsSerializable {

    private UserId userId;

    private Watch<T> watch;

    /**
     * For serialization purposes only
     */
    private UserWatch() {
    }

    public UserWatch(UserId userId, Watch<T> watch) {
        this.userId = checkNotNull(userId);
        this.watch = checkNotNull(watch);
    }

    public UserId getUserId() {
        return userId;
    }

    public Watch<T> getWatch() {
        return watch;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId, watch);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UserWatch)) {
            return false;
        }
        UserWatch other = (UserWatch) obj;
        return this.userId.equals(other.userId) && this.watch.equals(other.watch);
    }


    @Override
    public String toString() {
        return toStringHelper("UserWatch")
                .addValue(userId)
                .addValue(watch)
                .toString();
    }
}
