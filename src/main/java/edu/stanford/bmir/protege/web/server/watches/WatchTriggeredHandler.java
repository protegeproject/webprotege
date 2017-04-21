package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public interface WatchTriggeredHandler {

    /**
     * Handles a watch that was trigger on the specified entity
     * @param usersToNotify The users to notify that the watch has been triggered.
     * @param modifiedEntity The entity that was "changed" that triggered the watch.
     * @param byUser The user that made the change that triggered the watch
     */
    void handleWatchTriggered(@Nonnull Set<UserId> usersToNotify,
                              @Nonnull OWLEntity modifiedEntity,
                              @Nonnull UserId byUser);
}
