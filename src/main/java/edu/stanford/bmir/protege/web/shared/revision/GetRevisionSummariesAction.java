package edu.stanford.bmir.protege.web.shared.revision;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class GetRevisionSummariesAction implements Action<GetRevisionSummariesResult>, HasProjectId {

    private ProjectId projectId;

    /**
     * For serialization purposes only
     */
    private GetRevisionSummariesAction() {
    }

    public GetRevisionSummariesAction(ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
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
        if (!(obj instanceof GetRevisionSummariesAction)) {
            return false;
        }
        GetRevisionSummariesAction other = (GetRevisionSummariesAction) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetRevisionSummariesAction")
                .addValue(projectId)
                .toString();
    }
}
