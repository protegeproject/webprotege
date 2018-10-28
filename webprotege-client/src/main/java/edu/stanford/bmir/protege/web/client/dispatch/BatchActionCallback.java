package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.shared.dispatch.ActionExecutionResult;
import edu.stanford.bmir.protege.web.shared.dispatch.BatchResult;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Oct 2018
 *
 * A call back to handle the processing of batched results
 */
public class BatchActionCallback extends DispatchServiceCallback<BatchResult> {

    @Nonnull
    private final ImmutableList<PendingActionExecution<?, ?>> executions;

    /**
     * Creates a {@link BatchActionCallback} that will process the specified list
     * of pending action executions.
     * @param errorMessageDisplay An error display for the action.
     * @param executions A list of pending executions that will be processed as part of
     *                   the call back.
     */
    public BatchActionCallback(@Nonnull DispatchErrorMessageDisplay errorMessageDisplay,
                               @Nonnull ImmutableList<PendingActionExecution<?, ?>> executions) {
        super(errorMessageDisplay);
        this.executions = checkNotNull(executions);
    }

    @Override
    public void handleSubmittedForExecution() {
        super.handleSubmittedForExecution();
    }

    @Override
    public void handleSuccess(BatchResult batchResult) {
        ImmutableList<ActionExecutionResult> results = batchResult.getResults();
        if(results.size() != executions.size()) {
            throw new RuntimeException("Expected " + executions.size() + " results but only received " + results.size() + " results");
        }
        for(int i = 0; i < executions.size(); i++) {
            PendingActionExecution<?, ?> pendingActionExecution = executions.get(i);
            ActionExecutionResult result = results.get(i);
            AsyncCallback callback = pendingActionExecution.getCallback();
            result.getResult().ifPresent(callback::onSuccess);
            result.getActionExecutionException().ifPresent(callback::onFailure);
            result.getPermissionDeniedException().ifPresent(callback::onFailure);
        }
    }
}
