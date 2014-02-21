package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public interface WatchManager {

    /**
     * Gets the {@link Watch}es for the specified {@link UserId}.
     * @param userId The {@link UserId}.  Not {@code null}.
     * @return The set of {@link Watch} objects for the specified {@link UserId}.
     * @throws NullPointerException if {@code userId} is {@code null}.
     */
    Set<Watch<?>> getWatches(UserId userId);

    /**
     * Adds the specified watch for the specified user.
     *
     * @param watch The {@link edu.stanford.bmir.protege.web.shared.watches.Watch} to be added for the specified user. Not {@code null}.
     * @param userId The {@link edu.stanford.bmir.protege.web.shared.user.UserId} which specifies the user to add the watch for. Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    void addWatch(Watch<?> watch, UserId userId);

    /**
     * Removes the specified watch for the specified user.
     *
     * @param watch The {@link edu.stanford.bmir.protege.web.shared.watches.Watch} to be removed for the specified user.  Not {@code null}.
     * @param userId The {@link edu.stanford.bmir.protege.web.shared.user.UserId} that specifies the user.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    void removeWatch(Watch<?> watch, UserId userId);

    /**
     * Removes all watches for the specified user.
     * @param userId The {@link UserId} that specifies the user whose watches are to be removed.  Not {@code null}.
     * @throws NullPointerException if {@code userId} is {@code null}.
     */
    void clearWatches(UserId userId);

    Set<Watch<?>> getDirectWatches(Object watchedObject, UserId userId);

    boolean hasEntityBasedWatch(OWLEntity entity, UserId userId);





}
