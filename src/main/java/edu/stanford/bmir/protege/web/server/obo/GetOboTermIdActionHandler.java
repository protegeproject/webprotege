package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermIdAction;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermIdResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboTermIdActionHandler extends AbstractHasProjectActionHandler<GetOboTermIdAction, GetOboTermIdResult> {

    @Nonnull
    private final TermIdManager manager;

    @Inject
    public GetOboTermIdActionHandler(@Nonnull AccessManager accessManager,
                                     @Nonnull TermIdManager manager) {
        super(accessManager);
        this.manager = manager;
    }

    @Override
    public Class<GetOboTermIdAction> getActionClass() {
        return GetOboTermIdAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Override
    public GetOboTermIdResult execute(GetOboTermIdAction action, ExecutionContext executionContext) {
        return new GetOboTermIdResult(action.getTerm(), manager.getTermId(action.getTerm()));
    }
}
