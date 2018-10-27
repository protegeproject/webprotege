package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.UIObject;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyFieldView.EntityChangedHandler;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameSettingsManager;
import edu.stanford.bmir.protege.web.client.list.EntityNodeListPopupPresenter;
import edu.stanford.bmir.protege.web.client.list.EntityNodeListPopupPresenterFactory;
import edu.stanford.bmir.protege.web.client.renderer.ClassIriRenderer;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.protege.gwt.graphtree.shared.Path;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

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
    private final ClassIriRenderer classIriRenderer;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private EntityChangedHandler entityChangedHandler = () -> {};

    @Nonnull
    private final HierarchyPopupPresenterFactory hierarchyPopupPresenterFactory;

    @Nonnull
    private final DisplayNameSettingsManager displayNameSettingsManager;

    private Optional<HierarchyId> hierarchyId = Optional.empty();

    private Optional<HierarchyPopupPresenter> hierarchyPopupPresenter = Optional.empty();

    private WebProtegeEventBus eventBus;

    @Inject
    public HierarchyFieldPresenter(@Nonnull ProjectId projectId,
                                   @Nonnull HierarchyFieldView view,
                                   @Nonnull DispatchServiceManager dispatchServiceManager,
                                   @Nonnull EntityNodeListPopupPresenterFactory popupPresenterFactory,
                                   @Nonnull SelectionModel selectionModel,
                                   @Nonnull ClassIriRenderer classIriRenderer,
                                   @Nonnull Messages messages,
                                   @Nonnull HierarchyPopupPresenterFactory hierarchyPopupPresenterFactory,
                                   @Nonnull DisplayNameSettingsManager displayNameSettingsManager) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.dispatch = checkNotNull(dispatchServiceManager);
        this.popupPresenterFactory = checkNotNull(popupPresenterFactory);
        this.selectionModel = checkNotNull(selectionModel);
        this.classIriRenderer = checkNotNull(classIriRenderer);
        this.messages = checkNotNull(messages);
        this.hierarchyPopupPresenterFactory = checkNotNull(hierarchyPopupPresenterFactory);
        this.displayNameSettingsManager = checkNotNull(displayNameSettingsManager);
    }

    public void start(@Nonnull AcceptsOneWidget viewContainer,
                      @Nonnull WebProtegeEventBus eventBus) {
        this.eventBus = checkNotNull(eventBus);
        viewContainer.setWidget(view);
        view.setSyncClassWithLastSelectedClassHandler(this::handleSyncWithLastSelectedClass);
        view.setEntityChangedHandler(this::handleEntityChanged);
        view.setMoveToParentHandler(this::handleMoveToParent);
        view.setMoveToSiblingHandler(this::handleMoveToSibling);
        view.setMoveToChildHandler(this::handleMoveToChild);
        view.setShowPopupHierarchyHandler(this::handleShowPopupHierarchy);
        selectionModel.addSelectionChangedHandler(event -> updateButtonState());
        updateButtonState();
    }

    public void setSyncWithCurrentSelectionVisible(boolean visible) {
        view.setSyncWithCurrentSelectionVisible(visible);
    }

    private void handleShowPopupHierarchy(UIObject target) {
        hierarchyId.ifPresent(id -> {
            if(!hierarchyPopupPresenter.isPresent()) {
                HierarchyPopupPresenter hierarchyPopupPresenter = hierarchyPopupPresenterFactory.create(id);
                hierarchyPopupPresenter.start(eventBus);
                hierarchyPopupPresenter.setDisplayNameSettings(displayNameSettingsManager.getLocalDisplayNameSettings());
                this.hierarchyPopupPresenter = Optional.of(hierarchyPopupPresenter);
            }
            hierarchyPopupPresenter.ifPresent(presenter -> {
                view.getEntity().ifPresent(entity -> presenter.setSelectedEntity(entity.getEntity()));
                presenter.show(target, (sel) -> setEntityAndFireEvents(sel.getEntityData()));
                presenter.setDisplayNameSettings(displayNameSettingsManager.getLocalDisplayNameSettings());
            });

        });
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
        view.setSyncClassWithLastSelectedClassEnabled(
                selectionModel.getLastSelectedClass().isPresent()
                        && !selectionModel
                        .getLastSelectedClass()
                        .equals(view.getEntity().map(OWLEntityData::getEntity)));

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
                                 result -> view.setMoveToChildButtonEnabled(!result
                                         .getChildren()
                                         .getPageElements()
                                         .isEmpty()));
            });

        });
    }


    private void handleMoveToParent(UIObject target) {
        hierarchyId.ifPresent(id -> {
            view.getEntity()
                    .map(OWLEntityData::getEntity)
                    .ifPresent(entity -> getPathsToRootAndMoveToParent(id, entity, target));
        });
    }

    private void getPathsToRootAndMoveToParent(HierarchyId id, OWLEntity entity, UIObject target) {
        EntityNodeListPopupPresenter popup =
                popupPresenterFactory.create((pageRequest, consumer) -> {
                    dispatch.execute(new GetHierarchyPathsToRootAction(projectId,
                                                                       entity,
                                                                       id),
                                     result -> {
                                         List<EntityNode> data = result.getPaths()
                                                 .stream()
                                                 .map(Path::getLastPredecessor)
                                                 .filter(Optional::isPresent)
                                                 .map(Optional::get)
                                                 .distinct()
                                                 .map(GraphNode::getUserObject)
                                                 .collect(toList());
                                         Page<EntityNode> page = new Page<>(1, 1, data, data.size());
                                         consumer.consumeListData(page);
                                     });
                });
        popup.showRelativeTo(target, (sel) -> handlePopupClose(popup), messages.hierarchy_parents());
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
        popup.showRelativeTo(target, (sel) -> handlePopupClose(popup), messages.hierarchy_siblings());
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
        popup.showRelativeTo(target, (set) -> handlePopupClose(popup), messages.hierarchy_children());
    }

    private void handlePopupClose(EntityNodeListPopupPresenter popup) {
        popup.getFirstSelectedElement()
                .map(EntityNode::getEntityData)
                .ifPresent(this::setEntityAndFireEvents);
        view.requestFocus();
    }

    public Optional<OWLEntityData> getEntity() {
        return view.getEntity();
    }

    public void setEntity(OWLEntityData cls) {
        view.setEntity(cls);
        updateButtonState();
    }

    private void setEntityAndFireEvents(@Nonnull OWLEntityData entity) {
        setEntity(entity);
        entityChangedHandler.handleEntityChanged();
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
