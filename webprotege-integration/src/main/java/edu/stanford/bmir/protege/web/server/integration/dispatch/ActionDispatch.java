package edu.stanford.bmir.protege.web.server.integration.dispatch;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

/**
 * Port for executing WebProtégé project actions against the live ontology.
 * Implemented in {@code webprotege-server} by delegating to {@code ActionExecutor}.
 */
public interface ActionDispatch {

    @Nonnull
    <A extends Action<R>, R extends Result> R execute(@Nonnull A action, @Nonnull UserId userId);
}
