package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetAnnotationPropertyFrameAction implements Action<GetAnnotationPropertyFrameResult>, HasProjectId {

    private OWLAnnotationProperty subject;

    private ProjectId projectId;

    private GetAnnotationPropertyFrameAction() {
    }

    public GetAnnotationPropertyFrameAction(OWLAnnotationProperty subject, ProjectId projectId) {
        this.subject = subject;
        this.projectId = projectId;
    }

    public OWLAnnotationProperty getSubject() {
        return subject;
    }

    public ProjectId getProjectId() {
        return projectId;
    }
}
