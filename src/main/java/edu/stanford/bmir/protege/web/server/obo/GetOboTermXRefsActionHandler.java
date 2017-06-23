package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermXRefsAction;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermXRefsResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Jun 2017
 */
public class GetOboTermXRefsActionHandler extends AbstractHasProjectActionHandler<GetOboTermXRefsAction, GetOboTermXRefsResult> {

    @Nonnull
    private final TermXRefsManager xRefsManager;

    @Inject
    public GetOboTermXRefsActionHandler(@Nonnull AccessManager accessManager,
                                        @Nonnull TermXRefsManager xRefsManager) {
        super(accessManager);
        this.xRefsManager = xRefsManager;
    }

    @Override
    public Class<GetOboTermXRefsAction> getActionClass() {
        return GetOboTermXRefsAction.class;
    }

    @Override
    public GetOboTermXRefsResult execute(GetOboTermXRefsAction action, ExecutionContext executionContext) {
        return new GetOboTermXRefsResult(xRefsManager.getXRefs(action.getEntity()));
    }
}
