package edu.stanford.bmir.protege.web.shared.project;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class CreateNewProjectResult implements Result {

    private ProjectDetails projectDetails;

    /**
     * For serialization purposes only
     */
    private CreateNewProjectResult() {
    }

    public CreateNewProjectResult(ProjectDetails projectDetails) {
        this.projectDetails = checkNotNull(projectDetails);
    }

    public ProjectDetails getProjectDetails() {
        return projectDetails;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectDetails);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CreateNewProjectResult)) {
            return false;
        }
        CreateNewProjectResult other = (CreateNewProjectResult) obj;
        return this.projectDetails.equals(other.projectDetails);
    }


    @Override
    public String toString() {
        return toStringHelper("CreateNewProjectResult")
                .addValue(projectDetails)
                .toString();
    }
}
