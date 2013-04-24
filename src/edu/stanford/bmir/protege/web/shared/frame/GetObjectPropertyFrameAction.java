package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetObjectPropertyFrameAction implements HasProjectId, HasSubject<OWLObjectProperty>, GetObjectAction<LabelledFrame<ObjectPropertyFrame>> {


    private ProjectId projectId;

    private OWLObjectProperty subject;

    private GetObjectPropertyFrameAction() {

    }

    public GetObjectPropertyFrameAction(ProjectId projectId, OWLObjectProperty subject) {
        this.projectId = projectId;
        this.subject = subject;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public OWLObjectProperty getSubject() {
        return subject;
    }
}
