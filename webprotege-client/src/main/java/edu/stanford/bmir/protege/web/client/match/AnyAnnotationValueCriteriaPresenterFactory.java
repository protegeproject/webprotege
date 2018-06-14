package edu.stanford.bmir.protege.web.client.match;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class AnyAnnotationValueCriteriaPresenterFactory implements CriteriaPresenterFactory {

    @Nonnull
    private final Provider<AnyAnnotationValueCriteriaPresenter> presenterProvider;

    @Inject
    public AnyAnnotationValueCriteriaPresenterFactory(@Nonnull Provider<AnyAnnotationValueCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "has any value";
    }

    @Nonnull
    @Override
    public CriteriaPresenter createPresenter() {
        return presenterProvider.get();
    }
}
