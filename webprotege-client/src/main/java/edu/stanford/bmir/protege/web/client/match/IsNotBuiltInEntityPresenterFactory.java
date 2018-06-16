package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2018
 */
public class IsNotBuiltInEntityPresenterFactory implements CriteriaPresenterFactory<EntityMatchCriteria> {

    @Nonnull
    private final Provider<IsNotBuiltInEntityPresenter> presenterProvider;

    @Inject
    public IsNotBuiltInEntityPresenterFactory(@Nonnull Provider<IsNotBuiltInEntityPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Is not a built in entity";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends EntityMatchCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
