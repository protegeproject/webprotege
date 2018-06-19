package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.EntityCriteriaPresenter;

import javax.annotation.Nonnull;
import javax.inject.Inject;

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

}
