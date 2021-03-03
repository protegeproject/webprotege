package edu.stanford.bmir.protege.web.client.match;

import com.google.auto.factory.AutoFactory;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.renderer.ClassIriRenderer;
import edu.stanford.bmir.protege.web.shared.match.criteria.NotSubClassOfCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-06
 */
public class NotSubClassOfCriteriaPresenter implements CriteriaPresenter<NotSubClassOfCriteria> {


    @Nonnull
    private final ClassSelectorView view;

    @Nonnull
    private final ClassIriRenderer renderer;

    @Inject
    public NotSubClassOfCriteriaPresenter(@Nonnull ClassSelectorView view, @Nonnull ClassIriRenderer renderer) {
        this.view = checkNotNull(view);
        this.renderer = checkNotNull(renderer);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void stop() {

    }

    @Override
    public Optional<? extends NotSubClassOfCriteria> getCriteria() {
        view.getOwlClass()
            .map(cls -> NotSubClassOfCriteria.get(cls, view.getHierarchyFilterType()));
        return Optional.empty();
    }

    @Override
    public void setCriteria(@Nonnull NotSubClassOfCriteria criteria) {
        renderer.renderClassIri(criteria.getTarget().getIRI(), view::setOwlClass);
        view.setHierarchyFilterType(criteria.getFilterType());
    }
}
