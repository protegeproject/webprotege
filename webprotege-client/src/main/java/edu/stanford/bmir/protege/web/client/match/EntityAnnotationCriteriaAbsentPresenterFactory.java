package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.EntityAnnotationCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
public class EntityAnnotationCriteriaAbsentPresenterFactory implements CriteriaPresenterFactory<EntityAnnotationCriteria> {

    @Nonnull
    private final Provider<EntityAnnotationCriteriaAbsentPresenter> presenterProvider;

    @Inject
    public EntityAnnotationCriteriaAbsentPresenterFactory(@Nonnull Provider<EntityAnnotationCriteriaAbsentPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Does not have an annotation on";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends EntityAnnotationCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
