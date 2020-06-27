package edu.stanford.bmir.protege.web.client.match;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.viz.SelectableLiteralCriteriaPresenterFactory;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeLiteralCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-18
 */
public class LiteralCriteriaListPresenter extends CriteriaListPresenter<LiteralCriteria, CompositeLiteralCriteria> {

    @Inject
    public LiteralCriteriaListPresenter(@Nonnull CriteriaListView view, @Nonnull Provider<CriteriaListCriteriaViewContainer> viewContainerProvider,
                                        @Nonnull SelectableLiteralCriteriaPresenterFactory literalCriteriaPresenter) {
        super(view, viewContainerProvider, literalCriteriaPresenter);
    }

    @Override
    protected CompositeLiteralCriteria createCompositeCriteria(@Nonnull ImmutableList<? extends LiteralCriteria> criteriaList) {
        return CompositeLiteralCriteria.get(criteriaList, getMultiMatchType());
    }

    @Override
    protected ImmutableList<? extends LiteralCriteria> decomposeCompositeCriteria(CompositeLiteralCriteria compositeCriteria) {
        return compositeCriteria.getCriteria();
    }

    @Override
    protected MultiMatchType getMultiMatchType(CompositeLiteralCriteria compositeCriteria) {
        return compositeCriteria.getMultiMatchType();
    }
}
