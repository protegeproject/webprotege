package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermDefinitionAction;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermDefinitionResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class SetOboTermDefinitionActionHandler extends AbstractProjectActionHandler<SetOboTermDefinitionAction, SetOboTermDefinitionResult> {

    @Nonnull
    private final TermDefinitionManager termDefinitionManager;

    @Inject
    public SetOboTermDefinitionActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull TermDefinitionManager termDefinitionManager) {
        super(accessManager);
        this.termDefinitionManager = termDefinitionManager;
    }

    @Nonnull
    @Override
    public Class<SetOboTermDefinitionAction> getActionClass() {
        return SetOboTermDefinitionAction.class;
    }

    @Nonnull
    @Override
    public SetOboTermDefinitionResult execute(@Nonnull SetOboTermDefinitionAction action, @Nonnull ExecutionContext executionContext) {
        termDefinitionManager.setTermDefinition(executionContext.getUserId(),
                                                action.getEntity(),
                                                action.getDefinition());
        return new SetOboTermDefinitionResult();
    }
}
