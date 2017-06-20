package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class GetManchesterSyntaxFrameAction implements ProjectAction<GetManchesterSyntaxFrameResult>, HasSubject<OWLEntity> {

    private ProjectId projectId;

    private OWLEntity subject;

    /**
     * For serialization purposes only
     */
    private GetManchesterSyntaxFrameAction() {
    }

    public GetManchesterSyntaxFrameAction(ProjectId projectId, OWLEntity subject) {
        this.projectId = checkNotNull(projectId);
        this.subject = checkNotNull(subject);
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public OWLEntity getSubject() {
        return subject;
    }

    @Override
    public int hashCode() {
        return "GetManchesterSyntaxFrameAction".hashCode() + projectId.hashCode() + subject.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof GetManchesterSyntaxFrameAction)) {
            return false;
        }
        GetManchesterSyntaxFrameAction other = (GetManchesterSyntaxFrameAction) o;
        return this.projectId.equals(other.projectId) && this.subject.equals(other.subject);
    }
}
