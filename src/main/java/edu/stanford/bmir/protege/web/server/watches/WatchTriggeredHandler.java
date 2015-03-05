package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public interface WatchTriggeredHandler {

    void handleWatchTriggered(final Watch<?> watch, final UserId userId, final OWLEntity entity);
}
