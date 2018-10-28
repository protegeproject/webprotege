package edu.stanford.bmir.protege.web.shared.dispatch;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Oct 2018
 */
public class BatchAction implements Action<BatchResult> {

    private ImmutableList<Action<?>> actions;

    @GwtSerializationConstructor
    private BatchAction() {
    }


    /**
     * Create an action that batches together the specified actions.
     * @param actions The actions.
     * @return The batch action.
     */
    public static BatchAction create(@Nonnull ImmutableList<Action<?>> actions) {
        return new BatchAction(actions);
    }

    private BatchAction(@Nonnull ImmutableList<Action<?>> actions) {
        this.actions = checkNotNull(actions);
    }

    @Nonnull
    public ImmutableList<Action<?>> getActions() {
        return actions;
    }
}
