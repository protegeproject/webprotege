package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.InstanceOfCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2018
 */
public class InstanceOfCriteriaPresenterFactory implements CriteriaPresenterFactory<InstanceOfCriteria> {

    @Nonnull
    private final Provider<InstanceOfCriteriaPresenter> presenterProvider;

    @Inject
    public InstanceOfCriteriaPresenterFactory(@Nonnull Provider<InstanceOfCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Is an instance of";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends InstanceOfCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
