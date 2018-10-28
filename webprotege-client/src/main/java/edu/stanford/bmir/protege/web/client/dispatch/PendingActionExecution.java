package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.auto.value.AutoValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.DispatchServiceResultContainer;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Oct 2018
 *
 * Binds an action to a callback for later use.
 */
@AutoValue
public abstract class PendingActionExecution<A extends Action<R>, R extends Result> {

    @Nonnull
    public static <A extends Action<R>, R extends Result> PendingActionExecution<A, R> get(@Nonnull A action,
                                                                                           @Nonnull AsyncCallback<DispatchServiceResultContainer> result) {
        return new AutoValue_PendingActionExecution<>(action, result);
    }

    @Nonnull
    public abstract A getAction();

    @Nonnull
    public abstract AsyncCallback<DispatchServiceResultContainer> getCallback();
}
