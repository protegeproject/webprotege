package edu.stanford.bmir.protege.web.shared.project;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/16
 */
public class GetProjectDetailsAction implements Action<GetProjectDetailsResult>, HasProjectId {

    private ProjectId projectId;

    private GetProjectDetailsAction() {
    }

    public GetProjectDetailsAction(ProjectId projectId) {
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
        if (!(obj instanceof GetProjectDetailsAction)) {
            return false;
        }
        GetProjectDetailsAction other = (GetProjectDetailsAction) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetProjectDetailsAction")
                .addValue(projectId)
                .toString();
    }
}
