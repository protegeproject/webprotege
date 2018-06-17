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
public class EntityAnnotationMatchesAtMostOneCriteriaPresenterFactory implements CriteriaPresenterFactory<EntityAnnotationCriteria> {

    @Nonnull
    private final Provider<EntityAnnotationMatchesAtMostOneCriteriaPresenter> presenterProvider;

    @Inject
    public EntityAnnotationMatchesAtMostOneCriteriaPresenterFactory(@Nonnull Provider<EntityAnnotationMatchesAtMostOneCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Has at most one annotation on";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends EntityAnnotationCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
