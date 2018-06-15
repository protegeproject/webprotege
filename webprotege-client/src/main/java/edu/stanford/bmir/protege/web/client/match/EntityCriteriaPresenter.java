package edu.stanford.bmir.protege.web.client.match;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.RootCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public class EntityCriteriaPresenter extends CriteriaListPresenter<RootCriteria> {

    @Inject
    public EntityCriteriaPresenter(@Nonnull CriteriaListView view,
                                   @Nonnull Provider<CriteriaListCriteriaViewContainer> viewContainerProvider,
                                   @Nonnull RootCriteriaPresenterFactory presenterFactory) {
        super(view, viewContainerProvider, presenterFactory);
    }

    @Override
    protected RootCriteria createCriteria(@Nonnull ImmutableList<? extends RootCriteria> criteria) {
        return CompositeRootCriteria.get(criteria);
    }
}
