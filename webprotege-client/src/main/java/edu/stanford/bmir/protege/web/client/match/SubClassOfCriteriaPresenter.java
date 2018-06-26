package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.renderer.ClassIriRenderer;
import edu.stanford.bmir.protege.web.shared.match.criteria.SubClassOfCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
public class SubClassOfCriteriaPresenter implements CriteriaPresenter<SubClassOfCriteria> {

    @Nonnull
    private final ClassSelectorView view;

    @Nonnull
    private final ClassIriRenderer renderer;

    @Inject
    public SubClassOfCriteriaPresenter(@Nonnull ClassSelectorView view,
                                       @Nonnull ClassIriRenderer renderer) {
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
    public Optional<? extends SubClassOfCriteria> getCriteria() {
        return view.getOwlClass()
                   .map(cls -> SubClassOfCriteria.get(cls, view.getHierarchyFilterType()));
    }

    @Override
    public void setCriteria(@Nonnull SubClassOfCriteria criteria) {
        renderer.renderClassIri(criteria.getTarget().getIRI(),
                                view::setOwlClass);
        view.setHierarchyFilterType(criteria.getFilterType());
    }
}
