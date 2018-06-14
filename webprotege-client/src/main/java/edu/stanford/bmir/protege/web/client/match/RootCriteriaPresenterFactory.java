package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.RootCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public class RootCriteriaPresenterFactory implements CriteriaPresenterFactory<RootCriteria> {

    @Nonnull
    private final Provider<RootCriteriaPresenter> presenterProvider;

    @Inject
    public RootCriteriaPresenterFactory(@Nonnull Provider<RootCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Root Criteria";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends RootCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
