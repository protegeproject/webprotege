package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.ontology.id.GetOntologyIdAction;
import edu.stanford.bmir.protege.web.client.ontology.id.GetOntologyIdResult;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2013
 */
public class GetOntologyIdActionHandler extends AbstractHasProjectActionHandler<GetOntologyIdAction, GetOntologyIdResult> {

    @Nonnull
    @RootOntology
    private final OWLOntology rootOntology;

    public GetOntologyIdActionHandler(@Nonnull AccessManager accessManager,
                                      @Nonnull @RootOntology OWLOntology rootOntology) {
        super(accessManager);
        this.rootOntology = rootOntology;
    }

    @Override
    public GetOntologyIdResult execute(GetOntologyIdAction action, ExecutionContext executionContext) {
        return new GetOntologyIdResult(rootOntology.getOntologyID());
    }

    @Nonnull
    @Override
    protected RequestValidator getAdditionalRequestValidator(GetOntologyIdAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public Class<GetOntologyIdAction> getActionClass() {
        return GetOntologyIdAction.class;
    }
}
