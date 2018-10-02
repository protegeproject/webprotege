package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public class SetAnnotationValueUiAction extends AbstractUiAction {

    @Nonnull
    private final SetAnnotationValuePresenter presenter;

    @Nonnull
    private final BulkEditOperationWorkflowFactory workflowFactory;

    @Nonnull
    private Supplier<ImmutableSet<OWLEntityData>> selectionSupplier = ImmutableSet::of;

    @Inject
    public SetAnnotationValueUiAction(@Nonnull SetAnnotationValuePresenter presenter,
                                      @Nonnull BulkEditOperationWorkflowFactory workflowFactory) {
        super(presenter.getTitle() + "...");
        this.presenter = checkNotNull(presenter);
        this.workflowFactory = checkNotNull(workflowFactory);
    }

    public void setSelectionSupplier(@Nonnull Supplier<ImmutableSet<OWLEntityData>> selectionSupplier) {
        this.selectionSupplier = checkNotNull(selectionSupplier);
    }

    @Override
    public void execute() {
        ImmutableSet<OWLEntityData> sel = selectionSupplier.get();
        if(sel.isEmpty()) {
            return;
        }
        BulkEditOperationWorkflow workflow = workflowFactory.create(presenter, sel);
        workflow.start();
    }
}
