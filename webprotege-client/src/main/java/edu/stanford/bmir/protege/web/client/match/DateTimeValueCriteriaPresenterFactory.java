package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.AnnotationValueCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class DateTimeValueCriteriaPresenterFactory implements CriteriaPresenterFactory<AnnotationValueCriteria> {

    @Nonnull
    private final Provider<DateTimeValueCriteriaPresenter> presenterProvider;

    @Inject
    public DateTimeValueCriteriaPresenterFactory(@Nonnull Provider<DateTimeValueCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "is a date";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<AnnotationValueCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
