package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetAnnotationPropertyFrameAction implements ProjectAction<GetAnnotationPropertyFrameResult> {

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

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }
}
