package edu.stanford.bmir.protege.web.client.tag;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.RootCriteria;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.tag.TagData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2018
 */
public class TagCriteriaListPresenter {

    @Nonnull
    private final TagCriteriaListView view;

    @Nonnull
    private final Provider<TagCriteriaPresenter> presenterProvider;

    @Nonnull
    private final Provider<TagCriteriaViewContainer> tagCriteriaViewContainerProvider;

    private final List<TagCriteriaPresenter> presenters = new ArrayList<>();

    private final List<String> availableTagLabels = new ArrayList<>();

    @Inject
    public TagCriteriaListPresenter(@Nonnull TagCriteriaListView view,
                                    @Nonnull Provider<TagCriteriaPresenter> presenterProvider, @Nonnull Provider<TagCriteriaViewContainer> tagCriteriaViewContainerProvider) {
        this.view = checkNotNull(view);
        this.presenterProvider = checkNotNull(presenterProvider);
        this.tagCriteriaViewContainerProvider = checkNotNull(tagCriteriaViewContainerProvider);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        view.setAddHandler(this::handleAdd);
        // Add a blank criteria view
        addTagCriteriaView(false);
    }

    /**
     * Sets the tags labels that are available for having criteria added to them
     */
    public void setAvailableTags(@Nonnull List<String> availableTagLabels) {
        this.availableTagLabels.clear();
        this.availableTagLabels.addAll(availableTagLabels);
        // Update all existing presenters with the specified tag labels
        presenters.forEach(presenter -> presenter.setAvailableTags(availableTagLabels));
    }
    /**
     * Augments the specified tag data with criteria that have been edited by the user.
     * @param tagData The tag data to augment.
     * @return A list of the augmented tag data.
     */
    public List<TagData> augmentTagDataWithCriteria(@Nonnull List<TagData> tagData) {
        Multimap<String, RootCriteria> criteriaByTagLabel = getCriteriaByTagLabel();
        return tagData.stream()
                      // Map each TagData to itself if no criteria has been specified for it
                      // otherwise map it to a copy that contains the criteria
                      .map(td -> {
                          Collection<RootCriteria> criteria = criteriaByTagLabel.get(td.getLabel());
                          if (criteria.isEmpty()) {
                              return td;
                          }
                          else {
                              return td.withCriteria(ImmutableList.copyOf(criteria));
                          }
                      })
                      .collect(toList());
    }

    private void clear() {
        view.clearView();
        presenters.clear();
    }

    private TagCriteriaPresenter addTagCriteriaView(boolean scrollIntoView) {
        TagCriteriaPresenter presenter = presenterProvider.get();
        presenters.add(presenter);
        TagCriteriaViewContainer container = tagCriteriaViewContainerProvider.get();
        container.setRemoveHandler(() -> handleRemove(presenter));
        presenter.start(container.getViewContainer());
        presenter.setAvailableTags(availableTagLabels);
        view.addTagCriteriaViewContainer(container, scrollIntoView, scrollIntoView);
        return presenter;
    }

    private void handleRemove(TagCriteriaPresenter presenter) {
        int presenterIndex = presenters.indexOf(presenter);
        if (presenterIndex == -1) {
            return;
        }
        presenters.remove(presenterIndex);
        view.removeTagCriteriaViewContainer(presenterIndex);
    }

    private void handleAdd() {
        addTagCriteriaView(true);
    }

    /**
     * Gets the entered criteria grouped by tag label.  Each criteria implies the tag label.
     */
    private Multimap<String, RootCriteria> getCriteriaByTagLabel() {
        final Multimap<String, RootCriteria> criteriaByTagLabel = HashMultimap.create();
        presenters.forEach(presenter ->
                                   presenter.getSelectedTagLabel().ifPresent(label -> {
                                       presenter.getCriteria().ifPresent(criteria -> {
                                           criteriaByTagLabel.put(label, criteria);
                                       });
                                   }));
        return criteriaByTagLabel;
    }

    public void setTags(@Nonnull List<Tag> tags) {
        clear();
        tags.forEach(tag -> {
            tag.getCriteria().forEach(criteria -> {
                TagCriteriaPresenter presenter = addTagCriteriaView(false);
                presenter.setSelectedTagLabel(tag.getLabel());
                presenter.setCriteria(criteria);
            });
        });
    }
}
