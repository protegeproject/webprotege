package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLDataProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetDataPropertyFrameAction implements ProjectAction<GetDataPropertyFrameResult>, HasSubject<OWLDataProperty> {

    private OWLDataProperty subject;

    private ProjectId projectId;

    /**
     * For serialization purposes only
     */
    private GetDataPropertyFrameAction() {
    }

    public GetDataPropertyFrameAction(ProjectId projectId, OWLDataProperty subject) {
        this.projectId = projectId;
        this.subject = subject;
    }

    public OWLDataProperty getSubject() {
        return subject;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }


}
