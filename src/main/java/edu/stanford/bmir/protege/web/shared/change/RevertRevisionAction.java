package edu.stanford.bmir.protege.web.shared.change;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/03/15
 */
public class RevertRevisionAction implements Action<RevertRevisionResult>, HasProjectId {

    private RevisionNumber revisionNumber;

    private ProjectId projectId;

    /**
     * For serialization
     */
    private RevertRevisionAction() {
    }

    public RevertRevisionAction(ProjectId projectId, RevisionNumber revisionNumber) {
        this.projectId = checkNotNull(projectId);
        this.revisionNumber = checkNotNull(revisionNumber);
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public RevisionNumber getRevisionNumber() {
        return revisionNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, revisionNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RevertRevisionAction)) {
            return false;
        }
        RevertRevisionAction other = (RevertRevisionAction) obj;
        return this.projectId.equals(other.projectId) && this.revisionNumber.equals(other.revisionNumber);
    }


    @Override
    public String toString() {
        return toStringHelper("RevertRevisionAction")
                .addValue(projectId)
                .addValue(revisionNumber)
                .toString();
    }
}
