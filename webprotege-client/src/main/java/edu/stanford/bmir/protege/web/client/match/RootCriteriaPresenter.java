package edu.stanford.bmir.protege.web.client.match;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
public class RootCriteriaPresenter extends SelectableCriteriaTypePresenter {

    @Nonnull
    private final AnnotationCriteriaPresenterFactory annotationCriteriaPresenterFactory;

    @Nonnull
    private final IsDeprecatedCriteriaPresenterFactory isDeprecatedFactory;

    @Nonnull
    private final IsNotDeprecatedCriteriaPresenterFactory notDeprecatedFactory;

    @Inject
    public RootCriteriaPresenter(@Nonnull SelectableCriteriaTypeView view,
                                 @Nonnull AnnotationCriteriaPresenterFactory annotationCriteriaPresenterFactory,
                                 @Nonnull IsDeprecatedCriteriaPresenterFactory isDeprecatedFactory, @Nonnull IsNotDeprecatedCriteriaPresenterFactory notDeprecatedFactory) {
        super(view);
        this.annotationCriteriaPresenterFactory = checkNotNull(annotationCriteriaPresenterFactory);
        this.isDeprecatedFactory = checkNotNull(isDeprecatedFactory);
        this.notDeprecatedFactory = checkNotNull(notDeprecatedFactory);
    }

    @Override
    protected void start(@Nonnull PresenterFactoryRegistry factoryRegistry) {
        factoryRegistry.addPresenter(annotationCriteriaPresenterFactory);
        factoryRegistry.addPresenter(isDeprecatedFactory);
        factoryRegistry.addPresenter(notDeprecatedFactory);
    }
}
