package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.EntityIsCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-09
 */
public class EntityIsCriteriaPresenterFactory implements CriteriaPresenterFactory<EntityIsCriteria> {

    @Nonnull
    private final Provider<EntityIsCriteriaPresenter> presenterProvider;

    @Inject
    public EntityIsCriteriaPresenterFactory(@Nonnull Provider<EntityIsCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Is";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends EntityIsCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
