package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractProjectSpecificResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class GetRootOntologyIdResult extends AbstractProjectSpecificResult<OWLOntologyID> {

    /**
     * For serialization only
     */
    private GetRootOntologyIdResult() {
    }

    public GetRootOntologyIdResult(ProjectId projectId, OWLOntologyID object) {
        super(projectId, object);
    }
}
