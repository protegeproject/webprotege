package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.EntityIsNotCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-06
 */
public class EntityIsNotCriteriaPresenterFactory implements CriteriaPresenterFactory<EntityIsNotCriteria> {

    @Nonnull
    private final Provider<EntityIsNotCriteriaPresenter> presenterProvider;

    @Inject
    public EntityIsNotCriteriaPresenterFactory(@Nonnull Provider<EntityIsNotCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Is not";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends EntityIsNotCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
