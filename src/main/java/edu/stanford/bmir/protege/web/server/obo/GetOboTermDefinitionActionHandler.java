package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.obo.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Jun 2017
 */
public class GetOboTermDefinitionActionHandler extends AbstractHasProjectActionHandler<GetOboTermDefinitionAction, GetOboTermDefinitionResult> {

    @Nonnull
    private final TermDefinitionManager termDefinitionManager;

    @Inject
    public GetOboTermDefinitionActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull TermDefinitionManager termDefinitionManager) {
        super(accessManager);
        this.termDefinitionManager = termDefinitionManager;
    }

    @Override
    public Class<GetOboTermDefinitionAction> getActionClass() {
        return GetOboTermDefinitionAction.class;
    }

    @Override
    public GetOboTermDefinitionResult execute(GetOboTermDefinitionAction action, ExecutionContext executionContext) {
        return new GetOboTermDefinitionResult(termDefinitionManager.getTermDefinition(action.getTerm()));
    }




}
