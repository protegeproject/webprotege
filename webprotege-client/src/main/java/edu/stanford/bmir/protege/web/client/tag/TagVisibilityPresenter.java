package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.filter.FilterId;
import edu.stanford.bmir.protege.web.shared.filter.FilterSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.GetProjectTagsAction;
import edu.stanford.bmir.protege.web.shared.tag.Tag;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.tag.ProjectTagsStyleManager.getTagHiddenClassName;
import static edu.stanford.bmir.protege.web.shared.filter.FilterSetting.ON;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Mar 2018
 *
 * Interacts with a {@link FilterView} to control the visibility of tags displayed in some view.
 */
public class TagVisibilityPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    private Optional<IsWidget> tagsView = Optional.empty();

    private Optional<FilterView> filterView = Optional.empty();

    private final Map<FilterId, Tag> filterId2Tag = new HashMap<>();

    @Inject
    public TagVisibilityPresenter(@Nonnull ProjectId projectId,
                                  @Nonnull Messages messages,
                                  @Nonnull WebProtegeClientBundle clientBundle,
                                  @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = checkNotNull(projectId);
        this.messages = checkNotNull(messages);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    /**
     * Starts the tag visibility presenter.  The presenter is associated with a filter view
     * and some other high level view that contains tags (in possibly nested views – deep in
     * the DOM).
     * @param filterView The filter view that provides the visibility setting.
     * @param tagsView The view that contains tags.
     */
    public void start(@Nonnull FilterView filterView, @Nonnull IsWidget tagsView) {
        this.filterView = Optional.of(checkNotNull(filterView));
        this.tagsView = Optional.of(checkNotNull(tagsView));
        resetCurrentStyles();
        dispatchServiceManager.execute(new GetProjectTagsAction(projectId),
                                       result -> {
                                           filterView.addFilterGroup(messages.tags_displayedTags());
                                            result.getTags().forEach(tag -> {
                                                FilterId filterId = new FilterId(tag.getLabel());
                                                filterView.addFilter(
                                                        filterId,
                                                        ON
                                                );
                                                filterId2Tag.put(filterId, tag);
                                            });
                                            filterView.closeCurrentGroup();
                                       });
        filterView.addValueChangeHandler(this::handleFilterChanged);
    }

    private void handleFilterChanged(ValueChangeEvent<FilterSet> event) {
        filterView.ifPresent(v -> {
            FilterSet filterSet = v.getFilterSet();
            updateVisibility(filterSet);
        });
    }

    private void resetCurrentStyles() {
        removeCurrentTagStyles();
        filterId2Tag.clear();
    }

    private void removeCurrentTagStyles() {
        tagsView.ifPresent(v -> {
            filterId2Tag.values().forEach(tag -> {
                Widget widget = v.asWidget();
                widget.removeStyleName(getTagHiddenClassName(tag.getTagId()));
            });
        });
    }

    private void updateVisibility(FilterSet filterSet) {
        tagsView.ifPresent(v -> {
            Widget widget = v.asWidget();
            filterId2Tag.forEach((filterId, tag) -> {
                if(filterSet.hasSetting(filterId, ON)) {
                    widget.removeStyleName(getTagHiddenClassName(tag.getTagId()));
                }
                else {
                    widget.addStyleName(getTagHiddenClassName(tag.getTagId()));
                }
            });
        });
    }

}
