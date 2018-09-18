package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.UIObject;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyFieldView.EntityChangedHandler;
import edu.stanford.bmir.protege.web.client.list.EntityNodeListPopupPresenter;
import edu.stanford.bmir.protege.web.client.list.EntityNodeListPopupPresenterFactory;
import edu.stanford.bmir.protege.web.client.renderer.ClassIriRenderer;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.EntitySelectionChangedEvent;
import edu.stanford.bmir.protege.web.shared.selection.EntitySelectionChangedHandler;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
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
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final EntityNodeListPopupPresenterFactory popupPresenterFactory;

    @Nonnull
    private final SelectionModel selectionModel;

    @Nonnull
    private EntityChangedHandler entityChangedHandler = () -> {};

    private Optional<HierarchyId> hierarchyId = Optional.empty();

    @Nonnull
    private final ClassIriRenderer classIriRenderer;

    @Inject
    public HierarchyFieldPresenter(@Nonnull ProjectId projectId,
                                   @Nonnull HierarchyFieldView view,
                                   @Nonnull DispatchServiceManager dispatchServiceManager,
                                   @Nonnull EntityNodeListPopupPresenterFactory popupPresenterFactory,
                                   @Nonnull SelectionModel selectionModel,
                                   @Nonnull ClassIriRenderer classIriRenderer) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.dispatch = checkNotNull(dispatchServiceManager);
        this.popupPresenterFactory = checkNotNull(popupPresenterFactory);
        this.selectionModel = checkNotNull(selectionModel);
        this.classIriRenderer = checkNotNull(classIriRenderer);
    }

    public void start(@Nonnull AcceptsOneWidget viewContainer) {
        viewContainer.setWidget(view);
        view.setSyncClassWithLastSelectedClassHandler(this::handleSyncWithLastSelectedClass);
        view.setEntityChangedHandler(this::handleEntityChanged);
        view.setMoveToParentHandler(this::handleMoveToParent);
        view.setMoveToSiblingHandler(this::handleMoveToSibling);
        view.setMoveToChildHandler(this::handleMoveToChild);
        selectionModel.addSelectionChangedHandler(event -> {
            updateButtonState();
        });
        updateButtonState();
    }



    public void setEntityChangedHandler(@Nonnull EntityChangedHandler handler) {
        this.entityChangedHandler = checkNotNull(handler);
    }

    private void handleSyncWithLastSelectedClass() {
        selectionModel.getLastSelectedClass().ifPresent(cls -> {
            classIriRenderer.renderClassIri(cls.getIRI(), this::setEntityAndFireEvents);
        });
    }

    private void handleEntityChanged() {
        updateButtonState();
        entityChangedHandler.handleEntityChanged();
    }

    private void updateButtonState() {
        view.setSyncClassWithLastSelectedClassEnabled(selectionModel.getLastSelectedClass().isPresent());
        view.setMoveToParentButtonEnabled(false);
        view.setMoveToChildButtonEnabled(false);
        view.setMoveToSiblingButtonEnabled(false);
        view.getEntity().ifPresent(entity -> {
            OWLEntity e = entity.getEntity();
            view.setMoveToParentButtonEnabled(!e.isTopEntity());
            view.setMoveToSiblingButtonEnabled(!e.isTopEntity() && !e.isBottomEntity());
            hierarchyId.ifPresent(id -> {
                dispatch.execute(new GetHierarchyChildrenAction(projectId,
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
        dispatch.execute(new GetHierarchyPathsToRootAction(projectId,
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

    private void handleMoveToSibling(UIObject target) {
        hierarchyId.ifPresent(id -> {
            view.getEntity().map(OWLEntityData::getEntity)
                .ifPresent(entity -> getSiblingsAndMoveToSibling(id, entity, target));
        });
    }

    private void getSiblingsAndMoveToSibling(@Nonnull HierarchyId id,
                                             @Nonnull OWLEntity entity,
                                             @Nonnull UIObject target) {
        EntityNodeListPopupPresenter popup =
                popupPresenterFactory.create((pageRequest, consumer) -> {
                    dispatch.execute(new GetHierarchySiblingsAction(projectId,
                                                                    entity,
                                                                    id,
                                                                    pageRequest),
                                     result -> {
                                         Page<EntityNode> data = result.getSiblingsPage()
                                                                       .transform(GraphNode::getUserObject);
                                         consumer.consumeListData(data);
                                     });
                });
        popup.showRelativeTo(target, (sel) -> handlePopupClose(popup));
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
        EntityNodeListPopupPresenter popup =
                popupPresenterFactory.create(((pageRequest, consumer) -> {
                    dispatch.execute(new GetHierarchyChildrenAction(projectId,
                                                                    entity,
                                                                    id,
                                                                    pageRequest),
                                     result -> {
                                         Page<EntityNode> data = result.getChildren()
                                                                       .transform(GraphNode::getUserObject);
                                         consumer.consumeListData(data);
                                     });
                }));
        popup.showRelativeTo(target, (set) -> handlePopupClose(popup));
    }

    private void handlePopupClose(EntityNodeListPopupPresenter popup) {
        popup.getFirstSelectedElement()
             .map(EntityNode::getEntityData)
             .ifPresent(this::setEntityAndFireEvents);
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
