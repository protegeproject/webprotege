package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyFieldView.EntityChangedHandler;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
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
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private EntityChangedHandler entityChangedHandler = () -> {};

    private Optional<HierarchyId> hierarchyId = Optional.empty();

    @Inject
    public HierarchyFieldPresenter(@Nonnull ProjectId projectId,
                                   @Nonnull HierarchyFieldView view,
                                   @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
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
            view.setMoveToChildButtonEnabled(!e.isBottomEntity());
            view.setMoveToSiblingButtonEnabled(!e.isTopEntity() && !e.isBottomEntity());
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

    private void handleMoveToChild() {
        hierarchyId.ifPresent(id -> {
            view.getEntity().map(OWLEntityData::getEntity)
                .ifPresent(entity -> getChildrenAndMoveToChild(id, entity));
        });
    }

    private void getChildrenAndMoveToChild(@Nonnull HierarchyId id,
                                           @Nonnull OWLEntity entity) {
        dispatchServiceManager.execute(new GetHierarchyChildrenAction(projectId,
                                                                      entity,
                                                                      id),
                                       this::handleHierarchyChildren);
    }

    private void handleHierarchyChildren(GetHierarchyChildrenResult result) {
        result.getChildren()
              .getPageElements()
              .stream()
              .map(n -> n.getUserObject().getEntityData())
              .findFirst()
              .ifPresent(ed -> setEntityAndFireEvents(ed));

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
}
