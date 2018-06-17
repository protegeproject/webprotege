package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.SubClassOfCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
public class SubClassOfCriteriaPresenterFactory implements CriteriaPresenterFactory<SubClassOfCriteria> {

    @Nonnull
    private final Provider<SubClassOfCriteriaPresenter> presenterProvider;

    @Inject
    public SubClassOfCriteriaPresenterFactory(@Nonnull Provider<SubClassOfCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Is a subclass of";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends SubClassOfCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
