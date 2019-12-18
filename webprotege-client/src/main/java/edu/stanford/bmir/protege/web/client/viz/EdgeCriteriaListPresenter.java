package edu.stanford.bmir.protege.web.client.viz;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.*;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
import edu.stanford.bmir.protege.web.shared.viz.CompositeEdgeCriteria;
import edu.stanford.bmir.protege.web.shared.viz.EdgeCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public class EdgeCriteriaListPresenter extends CriteriaListPresenter<EdgeCriteria, CompositeEdgeCriteria> {

    @Inject
    public EdgeCriteriaListPresenter(@Nonnull CriteriaListView view,
                                     @Nonnull Provider<CriteriaListCriteriaViewContainer> viewContainerProvider,
                                     @Nonnull RootEdgeCriteriaPresenterFactory presenterFactory) {
        super(view, viewContainerProvider, presenterFactory);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        setDefaultMatchType(MultiMatchType.ANY);
        setDisplayAtLeastOneCriteria(false);
        super.start(container);
    }

    @Override
    public Optional<CompositeEdgeCriteria> getCriteria() {
        return (Optional<CompositeEdgeCriteria>) super.getCriteria();
    }

    @Override
    protected CompositeEdgeCriteria createCompositeCriteria(@Nonnull ImmutableList<? extends EdgeCriteria> criteriaList) {
        return CompositeEdgeCriteria.get(criteriaList, getMultiMatchType());
    }

    @Override
    protected ImmutableList<? extends EdgeCriteria> decomposeCompositeCriteria(CompositeEdgeCriteria compositeCriteria) {
        return compositeCriteria.getCriteria();
    }

    @Override
    protected MultiMatchType getMultiMatchType(CompositeEdgeCriteria compositeCriteria) {
        return compositeCriteria.getMatchType();
    }
}
