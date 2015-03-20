package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.UserWatch;
import edu.stanford.bmir.protege.web.shared.watches.Watch;

import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public interface WatchStore {

    Set<UserWatch<?>> getWatches();

    void addWatch(UserWatch<?> userWatch);

    void removeWatch(UserWatch<?> userWatch);
}
