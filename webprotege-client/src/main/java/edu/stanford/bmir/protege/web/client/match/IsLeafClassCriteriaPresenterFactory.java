package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.IsLeafClassCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-12-07
 */
public class IsLeafClassCriteriaPresenterFactory implements CriteriaPresenterFactory<IsLeafClassCriteria> {

    @Nonnull
    private final Provider<IsLeafClassCriteriaPresenter> presenterProvider;

    @Inject
    public IsLeafClassCriteriaPresenterFactory(@Nonnull Provider<IsLeafClassCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Is leaf class";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<IsLeafClassCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
