package edu.stanford.bmir.protege.web.client.entity;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2018
 */
public class MergeEntitiesUiAction extends AbstractUiAction {

    @Nonnull
    private final MergeEntitiesWorkflow workflow;

    @Inject
    public MergeEntitiesUiAction(@Nonnull MergeEntitiesWorkflow workflow,
                                 @Nonnull Messages messages) {
        super(messages.merge_mergeInto());
        this.workflow = checkNotNull(workflow);
    }

    @Override
    public void execute() {
        workflow.start();
    }
}
