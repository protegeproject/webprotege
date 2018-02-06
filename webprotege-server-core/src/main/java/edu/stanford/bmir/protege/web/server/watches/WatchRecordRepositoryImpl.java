package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.UpdateOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.watches.WatchRecord.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Apr 2017
 */
@ApplicationSingleton
public class WatchRecordRepositoryImpl implements WatchRecordRepository {

    private final Datastore datastore;

    @Inject
    public WatchRecordRepositoryImpl(@Nonnull Datastore datastore) {
        this.datastore = checkNotNull(datastore);
    }

    @Override
    public void ensureIndexes() {
        datastore.ensureIndexes(WatchRecord.class);
    }

    /**
     * Finds {@link WatchRecord}s for the specified user.
     * @param userId The user
     * @return The {@link WatchRecord}s for the specified user.
     */
    @Override
    public List<WatchRecord> findWatchRecords(@Nonnull ProjectId projectId,
                                              @Nonnull UserId userId) {
        Query<WatchRecord> query = datastore.createQuery(WatchRecord.class);
        return query
                .field(PROJECT_ID).equal(projectId)
                .field(USER_ID).equal(userId)
                .asList();
    }


    /**
     * Finds {@link WatchRecord}s for the specified entities.
     * @param entities The entities
     * @return The {@link WatchRecord}s for the specified entities. Any WatchRecord for that watches
     * one of the specified entities will be returned
     */
    @Override
    public List<WatchRecord> findWatchRecords(@Nonnull ProjectId projectId,
                                              @Nonnull Collection<? extends OWLEntity> entities) {
        Query<WatchRecord> query = datastore.createQuery(WatchRecord.class);
        return query
                .field(PROJECT_ID).equal(projectId)
                .field(ENTITY).in(entities)
                .asList();
    }

    /**
     * Finds {@link WatchRecord}s for the specified user and entities.
     * @param userId The {@link UserId}.
     * @param entities The {@link OWLEntity}.
     * @return The {@link WatchRecord}s for the specified user and entities.  Each watch record will specify
     * a watch for the specified user and one of the specifed entities.
     */
    @Override
    public List<WatchRecord> findWatchRecords(@Nonnull ProjectId projectId,
                                              @Nonnull UserId userId,
                                              @Nonnull Collection<? extends OWLEntity> entities) {
        Query<WatchRecord> query = datastore.createQuery(WatchRecord.class);
        return query
                .field(PROJECT_ID).equal(projectId)
                .field(USER_ID).equal(userId)
                .field(ENTITY).in(entities)
                .asList();
    }

    /**
     * Save a {@link WatchRecord}
     * @param watch The {@link WatchRecord} to be saved.
     */
    @Override
    public void saveWatchRecord(@Nonnull WatchRecord watch) {
        Query<WatchRecord> query = datastore.createQuery(WatchRecord.class)
                                            .field(PROJECT_ID).equal(watch.getProjectId())
                                            .field(USER_ID).equal(watch.getUserId())
                                            .field(ENTITY).equal(watch.getEntity());
        UpdateOperations<WatchRecord> update = datastore.createUpdateOperations(WatchRecord.class)
                                                  .set(PROJECT_ID, watch.getProjectId())
                                                  .set(USER_ID, watch.getUserId())
                                                  .set(ENTITY, watch.getEntity())
                                                  .set(TYPE, watch.getType());
        datastore.update(query, update, new UpdateOptions().upsert(true));
    }

    /**
     * Delete a {@link WatchRecord}.
     * @param watch The {@link WatchRecord} to be deleted.
     */
    @Override
    public void deleteWatchRecord(@Nonnull WatchRecord watch) {
        Query<WatchRecord> query = datastore.createQuery(WatchRecord.class)
                                            .field(PROJECT_ID).equal(watch.getProjectId())
                                            .field(USER_ID).equal(watch.getUserId())
                                            .field(ENTITY).equal(watch.getEntity())
                                            .field(TYPE).equal(watch.getType());
        datastore.findAndDelete(query);
    }


}
