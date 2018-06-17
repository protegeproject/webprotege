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
    private final EntityAnnotationCriteriaAbsentPresenterFactory absentAnnotationCriteriaFactory;

    @Nonnull
    private final EntityAnnotationMatchesAtMostOneCriteriaPresenterFactory atMostOneAnnotationFactory;

    @Nonnull
    private final EntityTypeCriteriaPresenterFactory entityTypeFactory;

    @Nonnull
    private final IsNotBuiltInEntityPresenterFactory notBuiltInEntityFactory;

    @Nonnull
    private final IsDeprecatedCriteriaPresenterFactory isDeprecatedFactory;

    @Nonnull
    private final IsNotDeprecatedCriteriaPresenterFactory notDeprecatedFactory;

    @Nonnull
    private final NonUniqueLangTagsCriteriaPresenterFactory nonUniqueLangTags;

    @Nonnull
    private final EntityAnnotationValuesAreNotDisjointCriteriaPresenterFactory notDisjointFactory;

    @Nonnull
    private final SubClassOfCriteriaPresenterFactory subClassOfFactory;

    @Inject
    public RootCriteriaPresenter(@Nonnull SelectableCriteriaTypeView view,
                                 @Nonnull EntityAnnotationCriteriaPresenterFactory annotationCriteriaFactory,
                                 @Nonnull EntityAnnotationCriteriaAbsentPresenterFactory absentAnnotationCriteriaFactory, @Nonnull EntityAnnotationMatchesAtMostOneCriteriaPresenterFactory atMostOneAnnotationFactory, @Nonnull EntityTypeCriteriaPresenterFactory entityTypeFactory,
                                 @Nonnull IsNotBuiltInEntityPresenterFactory notBuiltInEntityFactory, @Nonnull IsDeprecatedCriteriaPresenterFactory isDeprecatedFactory,
                                 @Nonnull IsNotDeprecatedCriteriaPresenterFactory notDeprecatedFactory,
                                 @Nonnull NonUniqueLangTagsCriteriaPresenterFactory nonUniqueLangTags, @Nonnull EntityAnnotationValuesAreNotDisjointCriteriaPresenterFactory notDisjointFactory, @Nonnull SubClassOfCriteriaPresenterFactory subClassOfFactory) {
        super(view);
        this.annotationCriteriaFactory = checkNotNull(annotationCriteriaFactory);
        this.absentAnnotationCriteriaFactory = checkNotNull(absentAnnotationCriteriaFactory);
        this.atMostOneAnnotationFactory = checkNotNull(atMostOneAnnotationFactory);
        this.entityTypeFactory = checkNotNull(entityTypeFactory);
        this.notBuiltInEntityFactory = checkNotNull(notBuiltInEntityFactory);
        this.isDeprecatedFactory = checkNotNull(isDeprecatedFactory);
        this.notDeprecatedFactory = checkNotNull(notDeprecatedFactory);
        this.nonUniqueLangTags = checkNotNull(nonUniqueLangTags);
        this.notDisjointFactory = checkNotNull(notDisjointFactory);
        this.subClassOfFactory = checkNotNull(subClassOfFactory);
    }

    @Override
    protected void start(@Nonnull PresenterFactoryRegistry<EntityMatchCriteria> factoryRegistry) {
        factoryRegistry.addPresenter(annotationCriteriaFactory);
        factoryRegistry.addPresenter(absentAnnotationCriteriaFactory);
        factoryRegistry.addPresenter(atMostOneAnnotationFactory);
        factoryRegistry.addPresenter(entityTypeFactory);
        factoryRegistry.addPresenter(isDeprecatedFactory);
        factoryRegistry.addPresenter(notDeprecatedFactory);
        factoryRegistry.addPresenter(notBuiltInEntityFactory);
        factoryRegistry.addPresenter(nonUniqueLangTags);
        factoryRegistry.addPresenter(notDisjointFactory);
        factoryRegistry.addPresenter(subClassOfFactory);
    }
}
