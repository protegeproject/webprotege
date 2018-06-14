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
    private final IsDeprecatedCriteriaPresenterFactory isDeprecatedFactory;

    @Nonnull
    private final IsNotDeprecatedCriteriaPresenterFactory notDeprecatedFactory;

    @Inject
    public RootCriteriaPresenter(@Nonnull SelectableCriteriaTypeView view,
                                 @Nonnull EntityAnnotationCriteriaPresenterFactory annotationCriteriaFactory, @Nonnull IsDeprecatedCriteriaPresenterFactory isDeprecatedFactory, @Nonnull IsNotDeprecatedCriteriaPresenterFactory notDeprecatedFactory) {
        super(view);
        this.annotationCriteriaFactory = annotationCriteriaFactory;
        this.isDeprecatedFactory = checkNotNull(isDeprecatedFactory);
        this.notDeprecatedFactory = checkNotNull(notDeprecatedFactory);
    }

    @Override
    protected void start(@Nonnull PresenterFactoryRegistry<EntityMatchCriteria> factoryRegistry) {
        factoryRegistry.addPresenter(annotationCriteriaFactory);
        factoryRegistry.addPresenter(isDeprecatedFactory);
        factoryRegistry.addPresenter(notDeprecatedFactory);
    }
}
