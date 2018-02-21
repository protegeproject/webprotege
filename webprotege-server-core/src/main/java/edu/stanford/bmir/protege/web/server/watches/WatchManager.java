package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 *
 * Manages the watches for a project and the firing of events related to the watches.
 */
@ProjectSingleton
public interface WatchManager {

    /**
     * Gets the {@link Watch}es for the specified {@link UserId}.
     * @param userId The {@link UserId}.  Not {@code null}.
     * @return The set of {@link Watch} objects for the specified {@link UserId}.
     * @throws NullPointerException if {@code userId} is {@code null}.
     */
    Set<Watch> getWatches(@Nonnull UserId userId);

    /**
     * Adds the specified watch for the specified user.
     *
     * @param watch The {@link Watch} to be added for the specified user. Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    void addWatch(@Nonnull Watch watch);

    /**
     * Removes the specified watch for the specified user.
     *
     * @param watch The {@link Watch} to be removed for the specified user.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    void removeWatch(@Nonnull Watch watch);

    /**
     * Gets the watches that are attached to the specified entity by the specified user.
     * @param watchedObject The entity to which the watches are attched.
     * @param userId The user who the watches belong to.
     * @return The watches.
     */
    Set<Watch> getDirectWatches(@Nonnull OWLEntity watchedObject,
                                @Nonnull UserId userId);

    /**
     * Gets all direct watches for the specfied entity.
     * @param watchedEntity The watched entity
     * @return Direct watches for the specified entity
     */
    Set<Watch> getDirectWatches(@Nonnull OWLEntity watchedEntity);
}
