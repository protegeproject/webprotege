package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.AnnotationCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class AnnotationCriteriaPresenterFactory implements CriteriaPresenterFactory<AnnotationCriteria> {

    @Nonnull
    private final Provider<AnnotationCriteriaPresenter> presenterProvider;

    @Inject
    public AnnotationCriteriaPresenterFactory(@Nonnull Provider<AnnotationCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Annotation Match";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<AnnotationCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
