package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.EntityRelationshipCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-04
 */
public class EntityRelationshipAbsentPresenterFactory implements CriteriaPresenterFactory<EntityRelationshipCriteria> {

    @Nonnull
    private final Provider<EntityRelationshipAbsentPresenter> presenterProvider;

    @Inject
    public EntityRelationshipAbsentPresenterFactory(@Nonnull Provider<EntityRelationshipAbsentPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Does not have a relationship on";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends EntityRelationshipCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
