package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermCrossProductAction;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermCrossProductResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Jun 2017
 */
public class SetOboTermCrossProductsActionHandler extends AbstractProjectActionHandler<SetOboTermCrossProductAction, SetOboTermCrossProductResult> {

    @Nonnull
    private final TermCrossProductsManager crossProductsManager;

    @Inject
    public SetOboTermCrossProductsActionHandler(@Nonnull AccessManager accessManager,
                                                @Nonnull TermCrossProductsManager crossProductsManager) {
        super(accessManager);
        this.crossProductsManager = crossProductsManager;
    }

    @Nonnull
    @Override
    public Class<SetOboTermCrossProductAction> getActionClass() {
        return SetOboTermCrossProductAction.class;
    }

    @Nonnull
    @Override
    public SetOboTermCrossProductResult execute(@Nonnull SetOboTermCrossProductAction action,
                                                @Nonnull ExecutionContext executionContext) {
        crossProductsManager.setCrossProduct(executionContext.getUserId(),
                                             action.getEntity(),
                                             action.getCrossProduct());
        return new SetOboTermCrossProductResult();
    }
}
