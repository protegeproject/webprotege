package edu.stanford.bmir.protege.web.client.tag;

import edu.stanford.bmir.protege.web.shared.tag.Tag;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2018
 */
public class TagListPresenter {

    private final TagListView view;

    private final Provider<TagView> tagViewProvider;

    @Inject
    public TagListPresenter(TagListView view, Provider<TagView> tagViewProvider) {
        this.view = checkNotNull(view);
        this.tagViewProvider = checkNotNull(tagViewProvider);
    }

    @Nonnull
    public TagListView getView() {
        return view;
    }

    public void setTags(Collection<Tag> tags) {
        view.clear();
        List<TagView> tagViews = tags.stream()
                .map(tag -> {
                    TagView tagView = tagViewProvider.get();
                    tagView.setLabel(tag.getLabel());
                    tagView.setDescription(tag.getDescription());
                    tagView.setColor(tag.getColor());
                    tagView.setBackgroundColor(tag.getBackgroundColor());
                    return tagView;
                }).collect(toList());
        view.setTagViews(tagViews);
    }
}
