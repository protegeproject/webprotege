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
public class GetOboTermDefinitionActionHandler extends AbstractHasProjectActionHandler<GetTermDefinitionAction, GetTermDefinitionResult> {

    @Nonnull
    private final TermDefinitionManager termDefinitionManager;

    @Inject
    public GetOboTermDefinitionActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull TermDefinitionManager termDefinitionManager) {
        super(accessManager);
        this.termDefinitionManager = termDefinitionManager;
    }

    @Override
    public Class<GetTermDefinitionAction> getActionClass() {
        return GetTermDefinitionAction.class;
    }

    @Override
    public GetTermDefinitionResult execute(GetTermDefinitionAction action, ExecutionContext executionContext) {
        return new GetTermDefinitionResult(termDefinitionManager.getTermDefinition(action.getTerm()));
    }




}
