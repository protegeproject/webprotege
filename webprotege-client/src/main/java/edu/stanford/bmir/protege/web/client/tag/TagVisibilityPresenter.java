package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.filter.FilterId;
import edu.stanford.bmir.protege.web.shared.filter.FilterSet;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
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
    private final FilterId filterId;

    private final String tagsHiddenClassName;

    private Optional<IsWidget> tagsView = Optional.empty();

    private Optional<FilterView> filterView = Optional.empty();

    private boolean tagsVisible = true;

    @Inject
    public TagVisibilityPresenter(@Nonnull Messages messages,
                                  @Nonnull WebProtegeClientBundle clientBundle) {
        this.tagsHiddenClassName = clientBundle.tags().tagsHidden();
        this.filterId = new FilterId(messages.tags_DisplayTags());
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
        filterView.addFilter(filterId, ON);
        filterView.addValueChangeHandler(this::handleFilterChanged);
        updateVisibility();
    }

    private void handleFilterChanged(ValueChangeEvent<FilterSet> event) {
        filterView.ifPresent(v -> {
            FilterSet filterSet = v.getFilterSet();
            this.tagsVisible = filterSet.hasSetting(filterId, ON);
            updateVisibility();
        });
    }

    private void updateVisibility() {
        tagsView.ifPresent(v -> {
            if(tagsVisible) {
                v.asWidget().getElement().removeClassName(tagsHiddenClassName);
            }
            else {
                v.asWidget().getElement().addClassName(tagsHiddenClassName);
            }
        });
    }

}
