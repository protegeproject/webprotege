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
import java.util.List;

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

    @Id
    @Nonnull
    @Property(USER_ID)
    private final UserId userId;

    @Property(LAST_LOGIN)
    private final long lastLogin;

    @Property(LAST_LOGOUT)
    private final long lastLogout;

    @Nonnull
    private final List<RecentProjectRecord> recentProjects;

    public UserActivityRecord(@Nonnull UserId userId,
                              long lastLogin,
                              long lastLogout,
                              @Nonnull List<RecentProjectRecord> recentProjects) {
        this.userId = userId;
        this.lastLogin = lastLogin;
        this.lastLogout = lastLogout;
        this.recentProjects = ImmutableList.copyOf(recentProjects);
    }

    public static UserActivityRecord get(UserId userId) {
        return new UserActivityRecord(userId, 0, 0, Collections.emptyList());
    }

    @Nonnull
    public UserId getUserId() {
        return userId;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public long getLastLogout() {
        return lastLogout;
    }

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
                && this.lastLogin == other.lastLogin
                && this.lastLogout == other.lastLogout
                && this.recentProjects.equals(other.recentProjects);
    }
}
