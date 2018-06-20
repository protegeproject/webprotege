package edu.stanford.bmir.protege.web.client.tag;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.EntityCriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.tag.Tag;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2018
 */
public class TagCriteriaPresenter {

    @Nonnull
    private final TagCriteriaView view;

    @Nonnull
    private final EntityCriteriaPresenter entityCriteriaPresenter;

    private final List<Tag> availableTags = new ArrayList<>();

    @Inject
    public TagCriteriaPresenter(@Nonnull TagCriteriaView view,
                                @Nonnull EntityCriteriaPresenter entityCriteriaPresenter) {
        this.view = checkNotNull(view);
        this.entityCriteriaPresenter = checkNotNull(entityCriteriaPresenter);
        this.entityCriteriaPresenter.setMatchTextPrefix("If entity matches");
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        entityCriteriaPresenter.start(view.getTagCriteriaContainer());
    }

    public void setAvailableTags(@Nonnull List<Tag> availableTags) {
        this.availableTags.clear();
        this.availableTags.addAll(availableTags);
        List<String> tagName = availableTags.stream()
                                            .map(Tag::getLabel)
                                            .collect(toList());
        view.setAvailableTagNames(tagName);
    }

}
