package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.AnnotationValueCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public class AnnotationValueCriteriaPresenterFactory implements CriteriaPresenterFactory<AnnotationValueCriteria> {

    @Nonnull
    private final Provider<AnnotationValueCriteriaPresenter> presenterProvider;

    @Inject
    public AnnotationValueCriteriaPresenterFactory(@Nonnull Provider<AnnotationValueCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Annotation value";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends AnnotationValueCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
