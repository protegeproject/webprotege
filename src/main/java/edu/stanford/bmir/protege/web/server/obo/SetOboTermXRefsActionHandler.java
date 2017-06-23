package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermXRefsAction;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermXRefsResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Jun 2017
 */
public class SetOboTermXRefsActionHandler extends AbstractHasProjectActionHandler<SetOboTermXRefsAction, SetOboTermXRefsResult> {

    @Nonnull
    private final TermXRefsManager xRefsManager;

    @Inject
    public SetOboTermXRefsActionHandler(@Nonnull AccessManager accessManager,
                                        @Nonnull TermXRefsManager xRefsManager) {
        super(accessManager);
        this.xRefsManager = xRefsManager;
    }

    @Override
    public Class<SetOboTermXRefsAction> getActionClass() {
        return SetOboTermXRefsAction.class;
    }

    @Override
    public SetOboTermXRefsResult execute(SetOboTermXRefsAction action, ExecutionContext executionContext) {
        xRefsManager.setXRefs(executionContext.getUserId(),
                              action.getEntity(),
                              action.getXrefs());
        return new SetOboTermXRefsResult();
    }
}
