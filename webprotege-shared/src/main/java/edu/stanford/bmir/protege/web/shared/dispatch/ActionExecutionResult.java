package edu.stanford.bmir.protege.web.shared.dispatch;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Oct 2018
 */
public class ActionExecutionResult implements IsSerializable {

    @Nullable
    private DispatchServiceResultContainer result;

    @Nullable
    private PermissionDeniedException permissionDeniedException;

    @Nullable
    private ActionExecutionException actionExecutionException;

    @GwtSerializationConstructor
    private ActionExecutionResult() {
    }

    private ActionExecutionResult(DispatchServiceResultContainer result, PermissionDeniedException permissionDeniedException, ActionExecutionException actionExecutionException) {
        this.result = result;
        this.permissionDeniedException = permissionDeniedException;
        this.actionExecutionException = actionExecutionException;
    }

    /**
     * Get an {@link ActionExecutionResult} for the results of an action.  The results is
     * wrapped in a {@link DispatchServiceResultContainer}.
     */
    public static ActionExecutionResult get(DispatchServiceResultContainer result) {
        return new ActionExecutionResult(result, null, null);
    }

    /**
     * Get an {@link ActionExecutionResult} for a permission denied exception
     * @param permissionDeniedException The exception.
     */
    public static ActionExecutionResult get(PermissionDeniedException permissionDeniedException) {
        return new ActionExecutionResult(null, permissionDeniedException, null);
    }

    /**
     * Get an {@link ActionExecutionResult} for an {@link ActionExecutionException}.
     */
    public static ActionExecutionResult get(ActionExecutionException actionExecutionException) {
        return new ActionExecutionResult(null, null, actionExecutionException);
    }

    @Nonnull
    public Optional<DispatchServiceResultContainer> getResult() {
        return Optional.ofNullable(result);
    }

    @Nonnull
    public Optional<PermissionDeniedException> getPermissionDeniedException() {
        return Optional.ofNullable(permissionDeniedException);
    }

    @Nonnull
    public Optional<ActionExecutionException> getActionExecutionException() {
        return Optional.ofNullable(actionExecutionException);
    }
}
