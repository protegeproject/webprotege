package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public interface WatchRecordRepository extends Repository {

    @Override
    void ensureIndexes();

    /**
     * Finds {@link WatchRecord}s for the specified user.
     * @param userId The user
     * @return The {@link WatchRecord}s for the specified user.
     */
    List<WatchRecord> findWatchRecords(@Nonnull ProjectId projectId,
                                       @Nonnull UserId userId);

    /**
     * Finds {@link WatchRecord}s for the specified entities.
     * @param entities The entities
     * @return The {@link WatchRecord}s for the specified entities. Any WatchRecord for that watches
     * one of the specified entities will be returned
     */
    List<WatchRecord> findWatchRecords(@Nonnull ProjectId projectId,
                                       @Nonnull Collection<? extends OWLEntity> entities);

    /**
     * Finds {@link WatchRecord}s for the specified user and entities.
     * @param userId The {@link UserId}.
     * @param entities The {@link OWLEntity}.
     * @return The {@link WatchRecord}s for the specified user and entities.  Each watch record will specify
     * a watch for the specified user and one of the specifed entities.
     */
    List<WatchRecord> findWatchRecords(@Nonnull ProjectId projectId,
                                       @Nonnull UserId userId,
                                       @Nonnull Collection<? extends OWLEntity> entities);

    /**
     * Save a {@link WatchRecord}
     * @param watch The {@link WatchRecord} to be saved.
     */
    void saveWatchRecord(@Nonnull WatchRecord watch);

    /**
     * Delete a {@link WatchRecord}.
     * @param watch The {@link WatchRecord} to be deleted.
     */
    void deleteWatchRecord(@Nonnull WatchRecord watch);
}
