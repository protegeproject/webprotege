package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.entity.EntityItemMapper;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.entity.CommentedEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.BrowserTextChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.issues.CommentPostedEvent;
import edu.stanford.bmir.protege.web.shared.issues.DiscussionThreadStatusChangedEvent;
import edu.stanford.bmir.protege.web.shared.issues.GetCommentedEntitiesAction;
import edu.stanford.bmir.protege.web.shared.issues.GetCommentedEntitiesResult;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.perspective.EntityTypePerspectiveMapper;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.of;
import static edu.stanford.bmir.protege.web.shared.event.BrowserTextChangedEvent.ON_BROWSER_TEXT_CHANGED;
import static edu.stanford.bmir.protege.web.shared.issues.CommentPostedEvent.ON_COMMENT_POSTED;
import static edu.stanford.bmir.protege.web.shared.issues.DiscussionThreadStatusChangedEvent.ON_STATUS_CHANGED;
import static edu.stanford.bmir.protege.web.shared.issues.Status.CLOSED;
import static edu.stanford.bmir.protege.web.shared.issues.Status.OPEN;
import static edu.stanford.bmir.protege.web.shared.pagination.PageRequest.requestPageWithSize;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 7 Mar 2017
 */
public class CommentedEntitiesPresenter {

    public static final int PAGE_SIZE = 50;

    private final ProjectId projectId;

    private final CommentedEntitiesView view;

    private final DispatchServiceManager dispatchServiceManager;

    private final SelectionModel selectionModel;

    @Nonnull
    private final EntityTypePerspectiveMapper perspectiveMapper;

    @Nonnull
    private final PlaceController placeController;

    private final Set<OWLEntity> currentEntites = new HashSet<>();

    private final List<CommentedEntityData> pageElements = new ArrayList<>();

    private HasBusy hasBusy = busy -> {
    };

    @Inject
    public CommentedEntitiesPresenter(@Nonnull ProjectId projectId,
                                      @Nonnull CommentedEntitiesView view,
                                      @Nonnull DispatchServiceManager dispatchServiceManager,
                                      @Nonnull SelectionModel selectionModel,
                                      @Nonnull EntityTypePerspectiveMapper perspectiveMapper,
                                      @Nonnull PlaceController placeController) {
        this.projectId = projectId;
        this.view = view;
        this.dispatchServiceManager = dispatchServiceManager;
        this.selectionModel = selectionModel;
        this.perspectiveMapper = perspectiveMapper;
        this.placeController = placeController;
    }

    public void setHasBusy(@Nonnull HasBusy hasBusy) {
        this.hasBusy = checkNotNull(hasBusy);
    }

    public void start(@Nonnull AcceptsOneWidget container, @Nonnull WebProtegeEventBus eventBus) {
        container.setWidget(view);
        view.setSelectionHandler(this::handleEntitySelected);
        view.setPageNumberChangedHandler(pageNumber -> reload());
        view.setSortingKeyChangedHandler(this::reload);
        view.setGoToEntityHandler(this::handleGoToEntity);
        reload();
        eventBus.addProjectEventHandler(projectId, ON_BROWSER_TEXT_CHANGED, this::handleBrowserTextChanged);
        eventBus.addProjectEventHandler(projectId, ON_COMMENT_POSTED, this::handleCommentPosted);
        eventBus.addProjectEventHandler(projectId, ON_STATUS_CHANGED, this::handleStatusChanged);
    }

    private void handleBrowserTextChanged(BrowserTextChangedEvent event) {
        if (currentEntites.contains(event.getEntity())) {
            reload();
        }
    }

    private void handleCommentPosted(CommentPostedEvent event) {
        event.getEntity().ifPresent(entityData -> reload());
    }

    private void handleStatusChanged(DiscussionThreadStatusChangedEvent event) {
        reload();
    }

    private void handleEntitySelected(SelectionEvent<CommentedEntityData> event) {
        selectionModel.setSelection(event.getSelectedItem().getEntityData().getEntity());
    }

    private void handleGoToEntity(@Nonnull OWLEntityData entityData) {
        Place place = placeController.getWhere();
        if(!(place instanceof ProjectViewPlace)) {
            return;
        }
        PerspectiveId nextPerspectiveId = perspectiveMapper.getPerspectiveId(entityData.getEntity().getEntityType());
        ProjectViewPlace currentPlace = (ProjectViewPlace) place;
        ProjectViewPlace.Builder nextPlaceBuilder = currentPlace.builder();
        nextPlaceBuilder.clearSelection();
        nextPlaceBuilder.withPerspectiveId(nextPerspectiveId);
        nextPlaceBuilder.clearSelection();
        EntityItemMapper.getItem(entityData.getEntity()).ifPresent(nextPlaceBuilder::withSelectedItem);
        placeController.goTo(nextPlaceBuilder.build());
    }

    private void reload() {
        GWT.log("[CommentedEntitiesPresenter] Reloading list data");
        dispatchServiceManager.execute(new GetCommentedEntitiesAction(projectId,
                                                                      "",
                                                                      of(OPEN, CLOSED),
                                                                      view.getSelectedSortingKey(),
                                                                      requestPageWithSize(view.getPageNumber(), PAGE_SIZE)),
                                       hasBusy,
                                       this::fillList);
    }

    private void fillList(GetCommentedEntitiesResult result) {
        Page<CommentedEntityData> entities = result.getEntities();
        view.setPageCount(entities.getPageCount());
        view.setPageNumber(entities.getPageNumber());
        pageElements.clear();
        pageElements.addAll(entities.getPageElements());
        view.setEntities(pageElements);
        currentEntites.clear();
        currentEntites.addAll(entities.getPageElements().stream()
                                      .map(c -> c.getEntityData().getEntity())
                                      .collect(toList()));
    }
}
