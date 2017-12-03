package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

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
}
