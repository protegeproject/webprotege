package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
public class RootCriteriaPresenter extends SelectableCriteriaTypePresenter<EntityMatchCriteria> {

    @Nonnull
    private final EntityAnnotationCriteriaPresenterFactory annotationCriteriaFactory;

    @Nonnull
    private final EntityTypeCriteriaPresenterFactory entityTypeFactory;

    @Nonnull
    private final IsDeprecatedCriteriaPresenterFactory isDeprecatedFactory;

    @Nonnull
    private final IsNotDeprecatedCriteriaPresenterFactory notDeprecatedFactory;

    @Nonnull
    private final NonUniqueLangTagsCriteriaPresenterFactory nonUniqueLangTags;

    @Inject
    public RootCriteriaPresenter(@Nonnull SelectableCriteriaTypeView view,
                                 @Nonnull EntityAnnotationCriteriaPresenterFactory annotationCriteriaFactory,
                                 @Nonnull EntityTypeCriteriaPresenterFactory entityTypeFactory,
                                 @Nonnull IsDeprecatedCriteriaPresenterFactory isDeprecatedFactory,
                                 @Nonnull IsNotDeprecatedCriteriaPresenterFactory notDeprecatedFactory,
                                 @Nonnull NonUniqueLangTagsCriteriaPresenterFactory nonUniqueLangTags) {
        super(view);
        this.annotationCriteriaFactory = checkNotNull(annotationCriteriaFactory);
        this.entityTypeFactory = entityTypeFactory;
        this.isDeprecatedFactory = checkNotNull(isDeprecatedFactory);
        this.notDeprecatedFactory = checkNotNull(notDeprecatedFactory);
        this.nonUniqueLangTags = checkNotNull(nonUniqueLangTags);
    }

    @Override
    protected void start(@Nonnull PresenterFactoryRegistry<EntityMatchCriteria> factoryRegistry) {
        factoryRegistry.addPresenter(annotationCriteriaFactory);
        factoryRegistry.addPresenter(entityTypeFactory);
        factoryRegistry.addPresenter(isDeprecatedFactory);
        factoryRegistry.addPresenter(notDeprecatedFactory);
        factoryRegistry.addPresenter(nonUniqueLangTags);
    }
}
