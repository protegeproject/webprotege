package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.shared.ontology.GetOntologyIdAction;
import edu.stanford.bmir.protege.web.shared.ontology.GetOntologyIdResult;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2013
 */
public class GetOntologyIdActionHandler extends AbstractProjectActionHandler<GetOntologyIdAction, GetOntologyIdResult> {

    @Nonnull
    @RootOntology
    private final OWLOntology rootOntology;

    public GetOntologyIdActionHandler(@Nonnull AccessManager accessManager,
                                      @Nonnull @RootOntology OWLOntology rootOntology) {
        super(accessManager);
        this.rootOntology = rootOntology;
    }

    @Nonnull
    @Override
    public GetOntologyIdResult execute(@Nonnull GetOntologyIdAction action, @Nonnull ExecutionContext executionContext) {
        return new GetOntologyIdResult(rootOntology.getOntologyID());
    }

    @Nonnull
    @Override
    protected RequestValidator getAdditionalRequestValidator(GetOntologyIdAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public Class<GetOntologyIdAction> getActionClass() {
        return GetOntologyIdAction.class;
    }
}
