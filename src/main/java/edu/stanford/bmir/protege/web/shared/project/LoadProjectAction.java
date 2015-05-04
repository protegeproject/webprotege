package edu.stanford.bmir.protege.web.shared.project;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
public class LoadProjectAction implements Action<LoadProjectResult>, HasProjectId {

    private ProjectId projectId;

    /**
     * For serialization purposes onlu
     */
    private LoadProjectAction() {

    }

    public LoadProjectAction(ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

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
        if (!(obj instanceof LoadProjectAction)) {
            return false;
        }
        LoadProjectAction other = (LoadProjectAction) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("LoadProjectAction")
                .addValue(projectId)
                .toString();
    }
}
