package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.EntityCriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.match.criteria.RootCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

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

    private final List<String> availableTagLabels = new ArrayList<>();

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

    public void setAvailableTags(@Nonnull List<String> availableTagLabels) {
        this.availableTagLabels.clear();
        this.availableTagLabels.addAll(availableTagLabels);
        view.setAvailableTagLabels(availableTagLabels);
    }

    @Nonnull
    public Optional<String> getSelectedTagLabel() {
        return view.getSelectedTagLabel();
    }

    @Nonnull
    public Optional<? extends RootCriteria> getCriteria() {
        return entityCriteriaPresenter.getCriteria();
    }

    public void setSelectedTagLabel(@Nonnull String selectedTagLabel) {
        view.setSelectedTagLabel(selectedTagLabel);
    }

    public void setCriteria(@Nonnull RootCriteria criteria) {
        entityCriteriaPresenter.setCriteria(criteria.asCompositeRootCriteria());
    }
}
