package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.ForbiddenView;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.progress.BusyView;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.GetProjectTagsAction;
import edu.stanford.bmir.protege.web.shared.tag.GetProjectTagsResult;
import edu.stanford.bmir.protege.web.shared.tag.Tag;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ENTITY_TAGS;
import static edu.stanford.bmir.protege.web.shared.tag.SetProjectTagsAction.setProjectTags;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Mar 2018
 */
public class ProjectTagsPresenter implements Presenter, HasBusy {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ProjectTagsView view;

    @Nonnull
    private final BusyView busyView;

    @Nonnull
    private final ForbiddenView forbiddenView;

    @Nonnull
    private final PlaceController placeController;

    @Nonnull
    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private Optional<Place> nextPlace = Optional.empty();

    @Nonnull
    private Optional<AcceptsOneWidget> container = Optional.empty();

    @Inject
    public ProjectTagsPresenter(@Nonnull ProjectId projectId, @Nonnull ProjectTagsView view,
                                @Nonnull BusyView busyView,
                                @Nonnull ForbiddenView forbiddenView,
                                @Nonnull PlaceController placeController, @Nonnull LoggedInUserProjectPermissionChecker permissionChecker, @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.busyView = checkNotNull(busyView);
        this.forbiddenView = checkNotNull(forbiddenView);
        this.placeController = checkNotNull(placeController);
        this.permissionChecker = checkNotNull(permissionChecker);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        this.container = Optional.of(container);
        view.setCancelButtonVisible(nextPlace.isPresent());
        view.setApplyChangesHandler(this::handleApplyChanges);
        view.setCancelChangesHandler(this::handleCancelChanges);
        container.setWidget(busyView);
        permissionChecker.hasPermission(EDIT_ENTITY_TAGS, canEditTags -> {
            if(canEditTags) {
                container.setWidget(view);
                displayProjectTags();
            }
            else {
                container.setWidget(forbiddenView);
            }
        });
    }

    @Override
    public void setBusy(boolean busy) {
        if(busy) {
            container.ifPresent(c -> c.setWidget(busyView));
        }
        else {
            container.ifPresent(c -> c.setWidget(view));
        }
    }

    public void setNextPlace(@Nonnull Optional<Place> nextPlace) {
        this.nextPlace = checkNotNull(nextPlace);
    }

    private void handleApplyChanges() {
        dispatchServiceManager.execute(setProjectTags(projectId, view.getTagData()),
                                       result -> nextPlace.ifPresent(placeController::goTo));
    }

    private void handleCancelChanges() {
        nextPlace.ifPresent(placeController::goTo);
    }

    private void displayProjectTags() {
        dispatchServiceManager.execute(new GetProjectTagsAction(projectId),
                                       this::displayProjectTags);
    }

    private void displayProjectTags(GetProjectTagsResult result) {
        List<Tag> tags = result.getTags();
        view.setTags(tags, result.getTagUsage());
    }
}
