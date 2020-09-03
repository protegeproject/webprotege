package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermRelationshipsAction;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermRelationshipsResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboTermRelationshipsActionHandler extends AbstractProjectActionHandler<GetOboTermRelationshipsAction, GetOboTermRelationshipsResult> {

    @Nonnull
    private final TermRelationshipsManager relationshipsManager;

    @Inject
    public GetOboTermRelationshipsActionHandler(@Nonnull AccessManager accessManager,
                                                @Nonnull TermRelationshipsManager relationshipsManager) {
        super(accessManager);
        this.relationshipsManager = relationshipsManager;
    }

    @Nonnull
    @Override
    public Class<GetOboTermRelationshipsAction> getActionClass() {
        return GetOboTermRelationshipsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetOboTermRelationshipsAction action) {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetOboTermRelationshipsResult execute(@Nonnull GetOboTermRelationshipsAction action,
                                                 @Nonnull ExecutionContext executionContext) {
        return new GetOboTermRelationshipsResult(relationshipsManager.getRelationships(action.getEntity().asOWLClass()));
    }
}
