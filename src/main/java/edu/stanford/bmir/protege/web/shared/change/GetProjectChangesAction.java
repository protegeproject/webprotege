package edu.stanford.bmir.protege.web.shared.change;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24/02/15
 */
public class GetProjectChangesAction implements Action<GetProjectChangesResult>, HasProjectId {

    private ProjectId projectId;

    @Nullable
    private OWLEntity subject;

    /**
     * For serialization purposes only
     */
    private GetProjectChangesAction() {
    }

    public GetProjectChangesAction(ProjectId projectId, Optional<OWLEntity> subject) {
        this.projectId = checkNotNull(projectId);
        this.subject = checkNotNull(subject).orElse(null);
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public java.util.Optional<OWLEntity> getSubject() {
        return Optional.ofNullable(subject);
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
                && java.util.Objects.equals(this.subject, other.subject);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetProjectChangesAction")
                .addValue(projectId)
                .addValue(subject)
                .toString();
    }
}
