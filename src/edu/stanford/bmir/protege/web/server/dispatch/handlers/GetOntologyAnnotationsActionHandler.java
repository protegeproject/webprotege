package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.RenderableGetObjectResult;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetOntologyAnnotationsAction;
import edu.stanford.bmir.protege.web.server.comparator.OntologyAnnotationsComparator;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import org.semanticweb.owlapi.model.OWLAnnotation;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class GetOntologyAnnotationsActionHandler extends AbstractHasProjectActionHandler<GetOntologyAnnotationsAction, RenderableGetObjectResult<Set<OWLAnnotation>>> {


    @Override
    public Class<GetOntologyAnnotationsAction> getActionClass() {
        return GetOntologyAnnotationsAction.class;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(GetOntologyAnnotationsAction action, RequestContext requestContext) {
        return new UserHasProjectReadPermissionValidator();
    }

    @Override
    protected RenderableGetObjectResult<Set<OWLAnnotation>> execute(GetOntologyAnnotationsAction action, OWLAPIProject project, ExecutionContext executionContext) {

        List<OWLAnnotation> result = new ArrayList<OWLAnnotation>(project.getRootOntology().getAnnotations());
        Collections.sort(result, new OntologyAnnotationsComparator(project));
        return new RenderableGetObjectResult<Set<OWLAnnotation>>(new LinkedHashSet<OWLAnnotation>(result), BrowserTextMap.build(project.getRenderingManager(), result));
    }

}
