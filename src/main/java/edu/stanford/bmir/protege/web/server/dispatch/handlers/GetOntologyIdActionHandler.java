package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.ui.ontology.id.GetOntologyIdAction;
import edu.stanford.bmir.protege.web.client.ui.ontology.id.GetOntologyIdResult;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2013
 */
public class GetOntologyIdActionHandler extends AbstractHasProjectActionHandler<GetOntologyIdAction, GetOntologyIdResult> {

    @Inject
    public GetOntologyIdActionHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);
    }

    @Override
    protected GetOntologyIdResult execute(GetOntologyIdAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new GetOntologyIdResult(project.getRootOntology().getOntologyID());
    }

    @Override
    protected RequestValidator<GetOntologyIdAction> getAdditionalRequestValidator(GetOntologyIdAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public Class<GetOntologyIdAction> getActionClass() {
        return GetOntologyIdAction.class;
    }
}
