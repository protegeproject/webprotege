package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.EntityTypeCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Jun 2018
 */
public class EntityTypeCriteriaPresenterFactory implements CriteriaPresenterFactory<EntityTypeCriteria> {

    @Nonnull
    private final Provider<EntityTypeCriteriaPresenter> presenterProvider;

    @Inject
    public EntityTypeCriteriaPresenterFactory(@Nonnull Provider<EntityTypeCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Kind";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends EntityTypeCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
