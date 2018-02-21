package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.shared.hierarchy.MoveHierarchyNodeAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.client.TreeNodeDropHandler;
import edu.stanford.protege.gwt.graphtree.shared.DropType;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 8 Dec 2017
 */
public class EntityHierarchyDropHandler implements TreeNodeDropHandler<EntityHierarchyNode> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public EntityHierarchyDropHandler(@Nonnull ProjectId projectId,
                                      @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = checkNotNull(projectId);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    @Nonnull
    private Optional<HierarchyId> hierarchyId = Optional.empty();


    public void start(@Nonnull HierarchyId hierarchyId) {
        this.hierarchyId = Optional.of(hierarchyId);
    }

    @Override
    public boolean isDropPossible(@Nonnull Path<EntityHierarchyNode> nodePath,
                                  @Nonnull Path<EntityHierarchyNode> targetPath,
                                  @Nonnull DropType dropType) {
        if(!hierarchyId.isPresent()) {
            return false;
        }
        if(nodePath.isEmpty()) {
            return false;
        }
        // Don't drop on self
        return !targetPath.getLast().equals(nodePath.getLast());
    }

    @Override
    public boolean isTextDropPossible(@Nonnull Path<EntityHierarchyNode> path,
                                      @Nonnull DropType dropType) {
        return false;
    }

    @Override
    public void handleDrop(@Nonnull Path<EntityHierarchyNode> nodePath,
                           @Nonnull Path<EntityHierarchyNode> targetPath,
                           @Nonnull DropType dropType,
                           @Nonnull DropEndHandler dropEndHandler) {
        GWT.log("[EntityHierarchyDropHandler] handleDrop. From: " + nodePath + " To: " + nodePath);
        if(!hierarchyId.isPresent()) {
            dropEndHandler.handleDropCancelled();
            return;
        }
        if(nodePath.isEmpty()) {
            dropEndHandler.handleDropCancelled();
            return;
        }
        if(nodePath.getLast().map(EntityHierarchyNode::getEntity).map(OWLObject::isTopEntity).orElse(false)) {
            dropEndHandler.handleDropCancelled();
            return;
        }
        // Don't drop on self
        if(targetPath.getLast().equals(nodePath.getLast())) {
            dropEndHandler.handleDropCancelled();
            return;
        }
        dispatchServiceManager.execute(new MoveHierarchyNodeAction(projectId,
                                                                   hierarchyId.get(),
                                                                   nodePath,
                                                                   targetPath,
                                                                   dropType),
                                       moveResult -> {
                                            if(moveResult.isMoved()) {
                                                dropEndHandler.handleDropComplete();
                                            }
                                            else {
                                                dropEndHandler.handleDropCancelled();
                                            }
                                       });
    }

    @Override
    public void handleTextDrop(@Nonnull String s,
                               @Nonnull Path<EntityHierarchyNode> path,
                               @Nonnull DropType dropType,
                               @Nonnull DropEndHandler dropEndHandler) {

    }
}
