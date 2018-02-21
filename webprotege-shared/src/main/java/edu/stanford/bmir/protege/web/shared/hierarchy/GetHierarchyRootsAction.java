package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 30 Nov 2017
 */
public class GetHierarchyRootsAction implements ProjectAction<GetHierarchyRootsResult> {

    private ProjectId projectId;

    private HierarchyId hierarchyId;

    public GetHierarchyRootsAction(@Nonnull ProjectId projectId,
                                   @Nonnull HierarchyId hierarchyId) {
        this.projectId = checkNotNull(projectId);
        this.hierarchyId = checkNotNull(hierarchyId);
    }

    @GwtSerializationConstructor
    private GetHierarchyRootsAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public HierarchyId getHierarchyId() {
        return hierarchyId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hierarchyId, projectId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetHierarchyRootsAction)) {
            return false;
        }
        GetHierarchyRootsAction other = (GetHierarchyRootsAction) obj;
        return this.hierarchyId.equals(other.hierarchyId)
                && this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetHierarchyRootsAction")
                          .addValue(projectId)
                          .addValue(hierarchyId)
                          .toString();
    }
}
