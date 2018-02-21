package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermIdAction;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermIdResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class SetOboTermIdActionHandler extends AbstractProjectActionHandler<SetOboTermIdAction, SetOboTermIdResult> {

    @Nonnull
    private final TermIdManager termIdManager;

    @Inject
    public SetOboTermIdActionHandler(@Nonnull AccessManager accessManager,
                                     @Nonnull TermIdManager termIdManager) {
        super(accessManager);
        this.termIdManager = termIdManager;
    }

    @Nonnull
    @Override
    public Class<SetOboTermIdAction> getActionClass() {
        return SetOboTermIdAction.class;
    }

    @Nonnull
    @Override
    public SetOboTermIdResult execute(@Nonnull SetOboTermIdAction action, @Nonnull ExecutionContext executionContext) {
        termIdManager.setTermId(executionContext.getUserId(),
                                action.getEntity(),
                                action.getOboTermId());
        return new SetOboTermIdResult();
    }
}
