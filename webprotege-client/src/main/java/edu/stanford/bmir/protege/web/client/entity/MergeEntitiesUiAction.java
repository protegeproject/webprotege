package edu.stanford.bmir.protege.web.client.entity;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.bulkop.BulkEditOperationWorkflow;
import edu.stanford.bmir.protege.web.client.bulkop.BulkEditOperationWorkflowFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2018
 */
public class MergeEntitiesUiAction extends AbstractUiAction {

    @Nonnull
    private final MergeEntitiesPresenter presenter;

    @Nonnull
    private final BulkEditOperationWorkflowFactory workflowFactory;

    @Nullable
    private Supplier<ImmutableSet<OWLEntityData>> selectionSupplier = null;

    @Inject
    public MergeEntitiesUiAction(@Nonnull Messages messages,
                                 @Nonnull MergeEntitiesPresenter presenter,
                                 @Nonnull BulkEditOperationWorkflowFactory workflowFactory) {
        super(messages.merge_mergeInto());
        this.presenter = checkNotNull(presenter);
        this.workflowFactory = checkNotNull(workflowFactory);
    }

    public void setSelectionSupplier(@Nonnull Supplier<ImmutableSet<OWLEntityData>> selectionSupplier) {
        this.selectionSupplier = checkNotNull(selectionSupplier);
    }

    public void setHierarchyId(@Nonnull HierarchyId hierarchyId) {
        presenter.setHierarchyId(hierarchyId);
    }

    @Override
    public void execute() {
        if(selectionSupplier == null) {
            throw new RuntimeException("Selection supplier not set");
        }
        ImmutableSet<OWLEntityData> entityData = selectionSupplier.get();
        BulkEditOperationWorkflow workflow = workflowFactory.create(presenter, entityData);
        workflow.start();
    }
}
