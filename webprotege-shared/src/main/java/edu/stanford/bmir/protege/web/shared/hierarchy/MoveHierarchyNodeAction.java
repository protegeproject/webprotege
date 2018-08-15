package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.shared.DropType;
import edu.stanford.protege.gwt.graphtree.shared.Path;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 8 Dec 2017
 */
public class MoveHierarchyNodeAction implements ProjectAction<MoveHierarchyNodeResult> {

    private ProjectId projectId;

    private HierarchyId hierarchyId;

    private Path<EntityNode> fromNodePath;

    private Path<EntityNode> toNodeParentPath;

    private DropType dropType;


    public MoveHierarchyNodeAction(@Nonnull ProjectId projectId,
                                   @Nonnull HierarchyId hierarchyId,
                                   @Nonnull Path<EntityNode> fromNodePath,
                                   @Nonnull Path<EntityNode> toNodeParentPath,
                                   @Nonnull DropType dropType) {
        this.projectId = checkNotNull(projectId);
        this.hierarchyId = checkNotNull(hierarchyId);
        this.fromNodePath = checkNotNull(fromNodePath);
        this.toNodeParentPath = checkNotNull(toNodeParentPath);
        this.dropType = checkNotNull(dropType);
    }

    @GwtSerializationConstructor
    private MoveHierarchyNodeAction() {
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

    @Nonnull
    public Path<EntityNode> getFromNodePath() {
        return fromNodePath;
    }

    @Nonnull
    public Path<EntityNode> getToNodeParentPath() {
        return toNodeParentPath;
    }

    @Nonnull
    public DropType getDropType() {
        return dropType;
    }
}
