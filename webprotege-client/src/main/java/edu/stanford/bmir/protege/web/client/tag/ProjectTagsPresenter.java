package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.app.ForbiddenView;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
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

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Mar 2018
 */
public class ProjectTagsPresenter implements Presenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ProjectTagsView projectTagsView;

    @Nonnull
    private final ForbiddenView forbiddenView;

    @Nonnull
    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final SettingsPresenter settingsPresenter;

    @Nonnull
    private final TagCriteriaListPresenter tagCriteriaListPresenter;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private Optional<EventBus> eventBus = Optional.empty();

    @Inject
    public ProjectTagsPresenter(@Nonnull ProjectId projectId,
                                @Nonnull ProjectTagsView projectTagsView,
                                @Nonnull ForbiddenView forbiddenView,
                                @Nonnull LoggedInUserProjectPermissionChecker permissionChecker,
                                @Nonnull DispatchServiceManager dispatchServiceManager,
                                @Nonnull SettingsPresenter settingsPresenter,
                                @Nonnull TagCriteriaListPresenter tagCriteriaListPresenter,
                                @Nonnull Messages messages) {
        this.projectId = checkNotNull(projectId);
        this.projectTagsView = checkNotNull(projectTagsView);
        this.forbiddenView = checkNotNull(forbiddenView);
        this.permissionChecker = checkNotNull(permissionChecker);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.settingsPresenter = checkNotNull(settingsPresenter);
        this.tagCriteriaListPresenter = checkNotNull(tagCriteriaListPresenter);
        this.messages = checkNotNull(messages);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        this.eventBus = Optional.of(eventBus);
        projectTagsView.setTagListChangedHandler(this::handleTagListChanged);
        settingsPresenter.start(container);
        settingsPresenter.setBusy(true);
        settingsPresenter.setApplySettingsHandler(this::handleApplyChanges);
        settingsPresenter.setCancelSettingsHandler(this::handleCancelChanges);
        settingsPresenter.setSettingsTitle(messages.tags_projectTagsTitle());
        permissionChecker.hasPermission(EDIT_ENTITY_TAGS, canEditTags -> {
            if(canEditTags) {
                settingsPresenter.addSection(messages.tags_EditProjectTags())
                                 .setWidget(projectTagsView);
                tagCriteriaListPresenter.start(settingsPresenter.addSection(messages.tags_tagAssigments_Title()));
                displayProjectTags(container);
            }
            else {
                container.setWidget(forbiddenView);
            }
        });
    }

    public void setNextPlace(@Nonnull Optional<Place> nextPlace) {
        settingsPresenter.setNextPlace(nextPlace);
    }

    private void handleApplyChanges() {
        getTagData().ifPresent(tagData -> {
            dispatchServiceManager.execute(setProjectTags(projectId, tagData),
                                           result -> settingsPresenter.goToNextPlace());
        });
    }

    @Nonnull
    private Optional<List<TagData>> getTagData() {
        // Get data for entered project tags
        List<TagData> tagData = projectTagsView.getTagData();
        Set<String> labels = new HashSet<>();
        for(TagData td : tagData) {
            boolean added = labels.add(td.getLabel());
            if(!added) {
                projectTagsView.showDuplicateTagAlert(td.getLabel());
                return Optional.empty();
            }
        }
        // Integrate criteria
        List<TagData> tagDataWithCriteria = tagCriteriaListPresenter.augmentTagDataWithCriteria(tagData);
        GWT.log(tagDataWithCriteria.toString());
        return Optional.of(tagDataWithCriteria);
    }

    private void handleCancelChanges() {

    }

    private void displayProjectTags(AcceptsOneWidget container) {
        dispatchServiceManager.execute(new GetProjectTagsAction(projectId),
                                       result -> displayProjectTags(result, container));
    }

    private void displayProjectTags(GetProjectTagsResult result, AcceptsOneWidget container) {
        dispatchServiceManager.beginBatch();
        List<Tag> tags = result.getTags();
        projectTagsView.setTags(tags, result.getTagUsage());
        tagCriteriaListPresenter.setAvailableTags(tags.stream().map(Tag::getLabel).collect(Collectors.toList()));
        tagCriteriaListPresenter.setTags(tags);
        dispatchServiceManager.executeCurrentBatch();
        settingsPresenter.setBusy(false);
    }


    private void handleTagListChanged() {
        tagCriteriaListPresenter.setAvailableTags(projectTagsView.getTagData().stream().map(TagData::getLabel).collect(Collectors.toList()));
    }
}
