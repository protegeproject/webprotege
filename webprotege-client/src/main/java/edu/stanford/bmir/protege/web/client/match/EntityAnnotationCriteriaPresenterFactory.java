package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public class EntityAnnotationCriteriaPresenterFactory implements CriteriaPresenterFactory<EntityMatchCriteria> {

    @Nonnull
    private final Provider<EntityAnnotationCriteriaPresenter> presenterProvider;

    @Inject
    public EntityAnnotationCriteriaPresenterFactory(@Nonnull Provider<EntityAnnotationCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Has an annotation on";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends EntityMatchCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
