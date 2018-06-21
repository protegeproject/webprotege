package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.core.client.GWT;
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
import edu.stanford.bmir.protege.web.shared.tag.TagData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ENTITY_TAGS;
import static edu.stanford.bmir.protege.web.shared.tag.SetProjectTagsAction.setProjectTags;
import static java.util.stream.Collectors.groupingBy;

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
    private final TagCriteriaListPresenter tagCriteriaListPresenter;

    @Nonnull
    private Optional<Place> nextPlace = Optional.empty();

    @Nonnull
    private Optional<AcceptsOneWidget> container = Optional.empty();

    @Inject
    public ProjectTagsPresenter(@Nonnull ProjectId projectId, @Nonnull ProjectTagsView view,
                                @Nonnull BusyView busyView,
                                @Nonnull ForbiddenView forbiddenView,
                                @Nonnull PlaceController placeController,
                                @Nonnull LoggedInUserProjectPermissionChecker permissionChecker,
                                @Nonnull DispatchServiceManager dispatchServiceManager,
                                @Nonnull TagCriteriaListPresenter tagCriteriaListPresenter) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.busyView = checkNotNull(busyView);
        this.forbiddenView = checkNotNull(forbiddenView);
        this.placeController = checkNotNull(placeController);
        this.permissionChecker = checkNotNull(permissionChecker);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.tagCriteriaListPresenter = checkNotNull(tagCriteriaListPresenter);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        this.container = Optional.of(container);
        view.setCancelButtonVisible(nextPlace.isPresent());
        view.setApplyChangesHandler(this::handleApplyChanges);
        view.setCancelChangesHandler(this::handleCancelChanges);
        view.setTagListChangedHandler(this::handleTagListChanged);
        container.setWidget(busyView);
        permissionChecker.hasPermission(EDIT_ENTITY_TAGS, canEditTags -> {
            if(canEditTags) {
                container.setWidget(view);
                displayProjectTags();
                tagCriteriaListPresenter.start(view.getTagCriteriaContainer());
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
        getTagData().ifPresent(tagData -> {
            dispatchServiceManager.execute(setProjectTags(projectId, tagData),
                                           result -> nextPlace.ifPresent(placeController::goTo));
        });
    }

    @Nonnull
    private Optional<List<TagData>> getTagData() {
        // Get data for entered project tags
        List<TagData> tagData = view.getTagData();
        Set<String> labels = new HashSet<>();
        for(TagData td : tagData) {
            boolean added = labels.add(td.getLabel());
            if(!added) {
                view.showDuplicateTagAlert(td.getLabel());
                return Optional.empty();
            }
        }
        // Integrate criteria
        List<TagData> tagDataWithCriteria = tagCriteriaListPresenter.augmentTagDataWithCriteria(tagData);
        GWT.log(tagDataWithCriteria.toString());
        return Optional.of(tagData);
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
        tagCriteriaListPresenter.setAvailableTags(tags.stream().map(Tag::getLabel).collect(Collectors.toList()));
        // TODO: Set criteria
    }


    private void handleTagListChanged() {
        tagCriteriaListPresenter.setAvailableTags(view.getTagData().stream().map(TagData::getLabel).collect(Collectors.toList()));
    }
}
