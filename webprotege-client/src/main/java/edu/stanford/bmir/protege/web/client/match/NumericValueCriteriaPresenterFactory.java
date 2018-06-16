package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.NumericValueCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class NumericValueCriteriaPresenterFactory implements CriteriaPresenterFactory<LiteralCriteria> {

    @Nonnull
    private final Provider<NumericValueCriteriaPresenter> presenterProvider;

    @Inject
    public NumericValueCriteriaPresenterFactory(@Nonnull Provider<NumericValueCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "is a number";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<LiteralCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
