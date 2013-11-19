package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.RenderableGetObjectResult;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import org.semanticweb.owlapi.model.OWLAnnotation;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/08/2013
 */
public class GetOntologyAnnotationsResult extends RenderableGetObjectResult<Set<OWLAnnotation>> {

    private GetOntologyAnnotationsResult() {
    }

    public GetOntologyAnnotationsResult(Set<OWLAnnotation> object, BrowserTextMap browserTextMap) {
        super(object, browserTextMap);
    }
}
