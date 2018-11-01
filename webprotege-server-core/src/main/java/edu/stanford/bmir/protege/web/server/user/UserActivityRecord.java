package edu.stanford.bmir.protege.web.server.user;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.project.RecentProjectRecord;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Mar 2017
 */
@Entity(noClassnameStored = true, value = "UserActivity")
public class UserActivityRecord {


    public static final String USER_ID = "userId";

    public static final String LAST_LOGIN = "lastLogin";

    public static final String LAST_LOGOUT = "lastLogout";

    /**
     * A constant for an unknown timestamp
     */
    public static final long UNKNOWN = 0;

    @Id
    @Property(USER_ID)
    private UserId userId;

    @Property(LAST_LOGIN)
    private Date lastLogin;

    @Property(LAST_LOGOUT)
    private Date lastLogout;

    private List<RecentProjectRecord> recentProjects;

    // For Morphia
    private UserActivityRecord() {
    }

    /**
     * Creates a {@link UserActivityRecord}
     * @param userId The {@link UserId} of the user that the record pertains to.
     * @param lastLogin The time of last login (may be 0 to indicate unknown).
     * @param lastLogout The time of last logout (may be 0 to indicate unknown)
     * @param recentProjects A list of recent projects.
     */
    public UserActivityRecord(@Nonnull UserId userId,
                              long lastLogin,
                              long lastLogout,
                              @Nonnull List<RecentProjectRecord> recentProjects) {
        this.userId = checkNotNull(userId);
        this.lastLogin = new Date(lastLogin);
        this.lastLogout = new Date(lastLogout);
        this.recentProjects = ImmutableList.copyOf(recentProjects);
    }

    /**
     * A factory method for creating a {@link UserActivityRecord} for a specific user with
     * default timestamps and an empty recent project list.
     * @param userId The userId.
     * @return The {@link UserActivityRecord}.
     */
    @Nonnull
    public static UserActivityRecord get(@Nonnull UserId userId) {
        return new UserActivityRecord(userId, UNKNOWN, UNKNOWN, Collections.emptyList());
    }

    /**
     * Gets the {@link UserId} that this record pertains to.
     * @return The {@link UserId}
     */
    @Nonnull
    public UserId getUserId() {
        return userId;
    }

    /**
     * Gets the timestamp of the last login.
     * @return The timestamp.
     */
    public long getLastLogin() {
        return lastLogin.getTime();
    }

    /**
     * Gets the timestamp of the last logout.
     * @return The timestamp
     */
    public long getLastLogout() {
        return lastLogout.getTime();
    }

    /**
     * Gets a list of recent projects.
     * @return A list of recent projects.  Possibly empty.
     */
    @Nonnull
    public List<RecentProjectRecord> getRecentProjects() {
        return ImmutableList.copyOf(recentProjects);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId, lastLogin, lastLogout, recentProjects);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UserActivityRecord)) {
            return false;
        }
        UserActivityRecord other = (UserActivityRecord) obj;
        return this.userId.equals(other.userId)
                && this.lastLogin.equals(other.lastLogin)
                && this.lastLogout.equals(other.lastLogout)
                && this.recentProjects.equals(other.recentProjects);
    }


    @Override
    public String toString() {
        return toStringHelper("UserActivityRecord" )
                .addValue(userId)
                .add("lastLogin", lastLogin.getTime())
                .add("lastLogout", lastLogout.getTime())
                .add("recentProjects", recentProjects)
                .toString();
    }
}
