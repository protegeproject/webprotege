package edu.stanford.bmir.protege.web.server.dispatch;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/02/2013
 */
@ThreadSafe
public interface ActionHandlerRegistry {

    /**
     * Gets an action handler for the given action.
     * @param action The action.  Not {@code null}.
     * @param <A> The action type.
     * @param <R> The result type.
     * @return An {@link ActionHandler} that can handle the specified action.  Not {@code null}.
     * @throws ActionHandlerNotFoundException if no {@link ActionHandler} was registered with this registry for the
     * specified {@link Action}.
     * @throws NullPointerException if {@code action} is {@code null}.
     */
    @Nonnull
    <A extends Action<R>, R extends Result> ActionHandler<A, R> getActionHandler(A action) throws ActionHandlerNotFoundException;

}
