package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.CriteriaListCriteriaViewContainer;
import edu.stanford.bmir.protege.web.client.match.CriteriaListPresenter;
import edu.stanford.bmir.protege.web.client.match.CriteriaListView;
import edu.stanford.bmir.protege.web.client.match.CriteriaPresenterFactory;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeHierarchyPositionCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.HierarchyPositionCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-08
 */
public class HierarchyPositionCriteriaListPresenter extends CriteriaListPresenter<HierarchyPositionCriteria, CompositeHierarchyPositionCriteria> {

    @Inject
    public HierarchyPositionCriteriaListPresenter(@Nonnull CriteriaListView view,
                                                  @Nonnull Provider<CriteriaListCriteriaViewContainer> viewContainerProvider,
                                                  @Nonnull HierarchyPositionCriteriaPresenterFactory presenterFactory) {
        super(view, viewContainerProvider, presenterFactory);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        super.start(container);
        setDefaultMatchType(MultiMatchType.ANY);
        setMatchTextPrefix("matches");
    }

    @Override
    protected CompositeHierarchyPositionCriteria createCompositeCriteria(@Nonnull ImmutableList<? extends HierarchyPositionCriteria> criteriaList) {
        return CompositeHierarchyPositionCriteria.get(criteriaList, MultiMatchType.ALL);
    }

    @Override
    protected ImmutableList<? extends HierarchyPositionCriteria> decomposeCompositeCriteria(
            CompositeHierarchyPositionCriteria compositeCriteria) {
        return compositeCriteria.getCriteria();
    }

    @Override
    protected MultiMatchType getMultiMatchType(CompositeHierarchyPositionCriteria compositeCriteria) {
        return compositeCriteria.getMatchType();
    }
}
