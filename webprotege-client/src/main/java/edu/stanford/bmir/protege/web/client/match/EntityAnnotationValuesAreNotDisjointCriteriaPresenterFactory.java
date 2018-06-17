package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.EntityAnnotationValuesAreNotDisjointCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
public class EntityAnnotationValuesAreNotDisjointCriteriaPresenterFactory implements CriteriaPresenterFactory<EntityAnnotationValuesAreNotDisjointCriteria> {

    @Nonnull
    private final Provider<EntityAnnotationValuesAreNotDisjointCriteriaPresenter> presenterProvider;

    @Inject
    public EntityAnnotationValuesAreNotDisjointCriteriaPresenterFactory(@Nonnull Provider<EntityAnnotationValuesAreNotDisjointCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Has non-disjoint annotation values";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends EntityAnnotationValuesAreNotDisjointCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
