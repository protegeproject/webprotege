package edu.stanford.bmir.protege.web.shared.crud;

import com.google.common.base.MoreObjects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class GetEntityCrudKitsAction implements ProjectAction<GetEntityCrudKitsResult> {

    private ProjectId projectId;

    @GwtSerializationConstructor
    private GetEntityCrudKitsAction() {
    }

    public GetEntityCrudKitsAction(@Nonnull ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public int hashCode() {
        return projectId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof GetEntityCrudKitsAction)) {
            return false;
        }
        GetEntityCrudKitsAction other = (GetEntityCrudKitsAction) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetEntityCrudKitsAction")
                .addValue(projectId)
                .toString();
    }
}

