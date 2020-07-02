package edu.stanford.bmir.protege.web.client.match;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRelationshipValueCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
import edu.stanford.bmir.protege.web.shared.match.criteria.RelationshipValueCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-23
 */
public class RelationshipValueCriteriaListPresenter extends CriteriaListPresenter<RelationshipValueCriteria, CompositeRelationshipValueCriteria> {

    private MultiMatchType matchType = MultiMatchType.ALL;

    @Inject
    public RelationshipValueCriteriaListPresenter(@Nonnull CriteriaListView view,
                                                  @Nonnull Provider<CriteriaListCriteriaViewContainer> viewContainerProvider,
                                                  @Nonnull RelationshipValueCriteriaPresenterFactory presenterFactory) {
        super(view, viewContainerProvider, presenterFactory);
    }

    @Override
    protected CompositeRelationshipValueCriteria createCompositeCriteria(@Nonnull ImmutableList<? extends RelationshipValueCriteria> criteriaList) {
        return CompositeRelationshipValueCriteria.get(matchType, ImmutableList.copyOf(criteriaList));
    }

    @Override
    protected ImmutableList<? extends RelationshipValueCriteria> decomposeCompositeCriteria(
            CompositeRelationshipValueCriteria compositeCriteria) {
        return compositeCriteria.getCriteria();
    }

    @Override
    protected MultiMatchType getMultiMatchType(CompositeRelationshipValueCriteria compositeCriteria) {
        return matchType;
    }
}
