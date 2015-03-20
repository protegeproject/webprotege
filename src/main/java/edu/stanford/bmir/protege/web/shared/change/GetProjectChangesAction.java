package edu.stanford.bmir.protege.web.shared.change;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.Filter;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24/02/15
 */
public class GetProjectChangesAction implements Action<GetProjectChangesResult>, HasProjectId {

    private ProjectId projectId;

    private Optional<OWLEntity> subject;

    /**
     * For serialization purposes only
     */
    private GetProjectChangesAction() {
    }

    public GetProjectChangesAction(ProjectId projectId, Optional<OWLEntity> subject) {
        this.projectId = checkNotNull(projectId);
        this.subject = checkNotNull(subject);
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public Optional<OWLEntity> getSubject() {
        return subject;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetProjectChangesAction)) {
            return false;
        }
        GetProjectChangesAction other = (GetProjectChangesAction) obj;
        return this.projectId.equals(other.projectId)
                && this.subject.equals(other.subject);
    }


    @Override
    public String toString() {
        return toStringHelper("GetProjectChangesAction")
                .addValue(projectId)
                .addValue(subject)
                .toString();
    }
}
