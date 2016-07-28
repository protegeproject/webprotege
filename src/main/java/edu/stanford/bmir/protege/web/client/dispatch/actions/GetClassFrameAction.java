package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectAction;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.GetClassFrameResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class GetClassFrameAction implements Action<GetClassFrameResult>, HasProjectId, HasSubject<OWLClass> {

    private OWLClass subject;

    private ProjectId projectId;

    /**
     * For serialization purposes only
     */
    private GetClassFrameAction() {

    }

    public GetClassFrameAction(OWLClass subject, ProjectId projectId) {
        this.subject = subject;
        this.projectId = projectId;
    }

    @Override
    public int hashCode() {
        return "GetClassFrameAction".hashCode() + super.hashCode();
    }

    /**
     * Get the {@link edu.stanford.bmir.protege.web.shared.project.ProjectId}.
     *
     * @return The {@link edu.stanford.bmir.protege.web.shared.project.ProjectId}.  Not {@code null}.
     */
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Gets the subject of this object.
     *
     * @return The subject.  Not {@code null}.
     */
    @Override
    public OWLClass getSubject() {
        return subject;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof GetClassFrameAction)) {
            return false;
        }
        GetClassFrameAction other = (GetClassFrameAction) obj;
        return this.projectId.equals(other.projectId) && this.subject.equals(other.subject);
    }

    @Override
    public String toString() {
        return toStringHelper("GetClassFrameAction")
                .addValue(projectId)
                .add("subject", subject)
                .toString();
    }
}
