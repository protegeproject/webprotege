package edu.stanford.bmir.protege.web.server.user;

import edu.stanford.bmir.protege.web.server.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.server.project.RecentProjectRecord;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.server.user.UserActivityRecord.*;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Mar 2017
 */
@ApplicationSingleton
public class UserActivityManager implements Repository {

    private final Datastore datastore;

    @Inject
    public UserActivityManager(Datastore datastore) {
        this.datastore = datastore;
    }


    @Override
    public void ensureIndexes() {
        datastore.ensureIndexes(UserActivityRecord.class);
    }

    public void save(UserActivityRecord record) {
        if(record.getUserId().isGuest()) {
            return;
        }
        datastore.save(record);
    }

    public Optional<UserActivityRecord> getUserActivityRecord(UserId userId) {
        if(userId.isGuest()) {
            return Optional.empty();
        }
        UserActivityRecord record = datastore.get(UserActivityRecord.class, userId);
        return Optional.ofNullable(record);
    }

    private UserActivityRecord getByUserId(UserId userId) {
        UserActivityRecord record = datastore.get(UserActivityRecord.class, userId);
        if (record == null) {
            datastore.save(record = UserActivityRecord.get(userId));
        }
        return record;
    }

    public void setLastLogin(@Nonnull UserId userId, long lastLogin) {
        if(userId.isGuest()) {
            return;
        }
        getByUserId(userId);
        Query<UserActivityRecord> query = queryByUserId(userId);
        UpdateOperations<UserActivityRecord> operations = datastore.createUpdateOperations(UserActivityRecord.class)
                                                                   .set(LAST_LOGIN, lastLogin);
        datastore.update(query, operations);
    }

    public void setLastLogout(@Nonnull UserId userId, long lastLogout) {
        if(userId.isGuest()) {
            return;
        }
        getByUserId(userId);
        Query<UserActivityRecord> query = queryByUserId(userId);
        UpdateOperations<UserActivityRecord> operations = datastore.createUpdateOperations(UserActivityRecord.class)
                                                                   .set(LAST_LOGOUT, lastLogout);
        datastore.update(query, operations);
    }

    public void addRecentProject(@Nonnull UserId userId, @Nonnull ProjectId projectId, long timestamp) {
        if(userId.isGuest()) {
            return;
        }
        UserActivityRecord record = getByUserId(userId);
        List<RecentProjectRecord> recentProjects = record.getRecentProjects().stream()
                                                         .filter(recentProject -> !recentProject.getProjectId()
                                                                                                .equals(projectId))
                                                         .sorted()
                                                         .collect(toList());
        recentProjects.add(0, new RecentProjectRecord(projectId, timestamp));
        UserActivityRecord replacement = new UserActivityRecord(
                record.getUserId(),
                record.getLastLogin(),
                record.getLastLogout(),
                recentProjects
        );
        save(replacement);
    }


    private Query<UserActivityRecord> queryByUserId(@Nonnull UserId userId) {
        return datastore.createQuery(UserActivityRecord.class)
                        .field(USER_ID).equal(userId);
    }
}
