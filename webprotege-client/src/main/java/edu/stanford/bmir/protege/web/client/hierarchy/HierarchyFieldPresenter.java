package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.UIObject;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyFieldView.EntityChangedHandler;
import edu.stanford.bmir.protege.web.client.list.EntityNodeListPopupPresenter;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Sep 2018
 */
public class HierarchyFieldPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final HierarchyFieldView view;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final Provider<EntityNodeListPopupPresenter> popupProvider;

    @Nonnull
    private EntityChangedHandler entityChangedHandler = () -> {};

    private Optional<HierarchyId> hierarchyId = Optional.empty();

    @Inject
    public HierarchyFieldPresenter(@Nonnull ProjectId projectId,
                                   @Nonnull HierarchyFieldView view,
                                   @Nonnull DispatchServiceManager dispatchServiceManager,
                                   @Nonnull Provider<EntityNodeListPopupPresenter> popupProvider) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.popupProvider = checkNotNull(popupProvider);
    }

    public void start(@Nonnull AcceptsOneWidget viewContainer) {
        viewContainer.setWidget(view);
        view.setEntityChangedHandler(this::handleEntityChanged);
        view.setMoveToParentHandler(this::handleMoveToParent);
        view.setMoveToSiblingHandler(this::handleMoveToSibling);
        view.setMoveToChildHandler(this::handleMoveToChild);
        updateButtonState();
    }

    public void setEntityChangedHandler(@Nonnull EntityChangedHandler handler) {
        this.entityChangedHandler = checkNotNull(handler);
    }

    private void handleEntityChanged() {
        updateButtonState();
        entityChangedHandler.handleEntityChanged();
    }

    private void updateButtonState() {
        view.setMoveToParentButtonEnabled(false);
        view.setMoveToChildButtonEnabled(false);
        view.setMoveToSiblingButtonEnabled(false);
        view.getEntity().ifPresent(entity -> {
            OWLEntity e = entity.getEntity();
            view.setMoveToParentButtonEnabled(!e.isTopEntity());
            view.setMoveToSiblingButtonEnabled(!e.isTopEntity() && !e.isBottomEntity());
            hierarchyId.ifPresent(id -> {
                dispatchServiceManager.execute(new GetHierarchyChildrenAction(projectId,
                                                                              e,
                                                                              id,
                                                                              PageRequest.requestPageWithSize(1, 1)),
                                               result -> view.setMoveToChildButtonEnabled(!result.getChildren().getPageElements().isEmpty()));
            });

        });
    }


    private void handleMoveToParent() {
        hierarchyId.ifPresent(id -> {
            view.getEntity()
                .map(OWLEntityData::getEntity)
                .ifPresent(entity -> getPathsToRootAndMoveToParent(id, entity));
        });
    }

    private void getPathsToRootAndMoveToParent(HierarchyId id, OWLEntity entity) {
        dispatchServiceManager.execute(new GetHierarchyPathsToRootAction(projectId,
                                                                         entity,
                                                                         id),
                                       this::handlePathsToRoot);
    }

    private void handlePathsToRoot(GetHierarchyPathsToRootResult result) {
        result.getPaths().stream()
              .findFirst()
              .ifPresent(path -> path.getLastPredecessor()
                                     .map(last -> last.getUserObject().getEntityData())
                                     .ifPresent(this::setEntityAndFireEvents));
    }

    private void handleMoveToSibling() {

    }

    private void handleMoveToChild(UIObject target) {
        hierarchyId.ifPresent(id -> {
            view.getEntity().map(OWLEntityData::getEntity)
                .ifPresent(entity -> getChildrenAndMoveToChild(id, entity, target));
        });
    }

    private void getChildrenAndMoveToChild(@Nonnull HierarchyId id,
                                           @Nonnull OWLEntity entity,
                                           @Nonnull UIObject target) {
        dispatchServiceManager.execute(new GetHierarchyChildrenAction(projectId,
                                                                      entity,
                                                                      id),
                                       result -> handleHierarchyChildren(result, target));
    }

    private void handleHierarchyChildren(GetHierarchyChildrenResult result, UIObject target) {
        GWT.log("[HierarchyFieldPresenter] Showing children: " + result);
        List<GraphNode<EntityNode>> pageElements = result.getChildren()
                                                         .getPageElements();
        if(pageElements.size() == 1) {
            setEntityAndFireEvents(pageElements.get(0).getUserObject().getEntityData());
        }
        else {
            EntityNodeListPopupPresenter popup = popupProvider.get();
            popup.setListData(result.getChildren().transform(GraphNode::getUserObject));
            popup.showRelativeTo(target, (sel) -> {
                popup.getFirstSelectedElement()
                     .map(EntityNode::getEntityData)
                     .ifPresent(this::setEntityAndFireEvents);
            });
        }
    }

    public Optional<OWLEntityData> getEntity() {
        return view.getEntity();
    }

    private void setEntityAndFireEvents(@Nonnull OWLEntityData entity) {
        setEntity(entity);
        entityChangedHandler.handleEntityChanged();
    }

    public void setEntity(OWLEntityData cls) {
        view.setEntity(cls);
        updateButtonState();
    }

    public void setEntityType(PrimitiveType entityType) {
        view.setEntityType(entityType);
    }

    public void setHierarchyId(@Nonnull HierarchyId hierarchyId) {
        this.hierarchyId = Optional.of(hierarchyId);
    }

    public void clearEntity() {
        view.clearEntity();
    }
}
