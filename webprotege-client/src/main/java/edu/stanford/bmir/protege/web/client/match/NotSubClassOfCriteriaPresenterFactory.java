package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.NotSubClassOfCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-06
 */
public class NotSubClassOfCriteriaPresenterFactory implements CriteriaPresenterFactory<NotSubClassOfCriteria> {

    @Nonnull
    private final Provider<NotSubClassOfCriteriaPresenter> presenterProvider;

    @Inject
    public NotSubClassOfCriteriaPresenterFactory(@Nonnull Provider<NotSubClassOfCriteriaPresenter> presenterProvider) {
        this.presenterProvider = presenterProvider;
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Is not a subclass of";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends NotSubClassOfCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
