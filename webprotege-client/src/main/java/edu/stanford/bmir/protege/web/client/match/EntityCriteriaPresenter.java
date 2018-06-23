package edu.stanford.bmir.protege.web.client.match;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
import edu.stanford.bmir.protege.web.shared.match.criteria.RootCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public class EntityCriteriaPresenter extends CriteriaListPresenter<RootCriteria, CompositeRootCriteria> {

    @Inject
    public EntityCriteriaPresenter(@Nonnull CriteriaListView view,
                                   @Nonnull Provider<CriteriaListCriteriaViewContainer> viewContainerProvider,
                                   @Nonnull RootCriteriaPresenterFactory presenterFactory) {
        super(view, viewContainerProvider, presenterFactory);
        view.setMatchTextPrefix("Entities that match");
    }

    @Override
    protected CompositeRootCriteria createCompositeCriteria(@Nonnull ImmutableList<? extends RootCriteria> criteriaList) {
        return CompositeRootCriteria.get(criteriaList, getMultiMatchType());
    }

    @Override
    protected ImmutableList<? extends RootCriteria> decomposeCompositeCriteria(CompositeRootCriteria compositeCriteria) {
        return compositeCriteria.getRootCriteria();
    }

    @Override
    protected MultiMatchType getMultiMatchType(CompositeRootCriteria compositeCriteria) {
        return compositeCriteria.getMatchType();
    }
}
