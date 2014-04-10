package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.RenderableGetObjectResult;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetEntityAnnotationsAction;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class GetEntityAnnotationsActionHandler extends AbstractHasProjectActionHandler<GetEntityAnnotationsAction, RenderableGetObjectResult<Set<OWLAnnotation>>> {

    @Override
    public Class<GetEntityAnnotationsAction> getActionClass() {
        return GetEntityAnnotationsAction.class;
    }

    @Override
    protected RequestValidator<GetEntityAnnotationsAction> getAdditionalRequestValidator(GetEntityAnnotationsAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    protected RenderableGetObjectResult<Set<OWLAnnotation>> execute(GetEntityAnnotationsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        Set<OWLAnnotation> result = new HashSet<OWLAnnotation>();
        for(OWLOntology ontology : project.getRootOntology().getImportsClosure()) {
            for (OWLAnnotationAssertionAxiom ax : ontology.getAnnotationAssertionAxioms(action.getSubject())) {
                result.add(ax.getAnnotation());
            }
        }
        BrowserTextMap browserTextMap = new BrowserTextMap(result, project.getRenderingManager());
        return new RenderableGetObjectResult<Set<OWLAnnotation>>(result, browserTextMap);
    }
}
