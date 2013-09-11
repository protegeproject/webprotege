package edu.stanford.bmir.protege.web.shared.irigen.obo;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public class UserIdRange implements Serializable {

    private UserId userId;

    private long start;

    private long end;

    /**
     * For serialization purposes only
     */
    private UserIdRange() {
    }

    public UserIdRange(UserId userId, long start, long end) {
        this.userId = userId;
        this.start = start;
        this.end = end;
    }

    public static long getDefaultEnd() {
        return Long.MAX_VALUE;
    }

    public static long getDefaultStart() {
        return 0;
    }

    public UserId getUserId() {
        return userId;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("UserIdRange")
                .add("userId", userId)
                .add("start", start)
                .add("end", end)
                .toString();
    }
}
