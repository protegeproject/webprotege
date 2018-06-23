package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.AnnotationValueCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.DateCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.DateIsBeforeCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class DateTimeValueCriteriaPresenter extends SelectableCriteriaTypePresenter<AnnotationValueCriteria> {

    @Nonnull
    private final DateIsBeforePresenterFactory beforeFactory;

    @Nonnull
    private final DateIsAfterPresenterFactory afterFactory;

    @Inject
    public DateTimeValueCriteriaPresenter(@Nonnull SelectableCriteriaTypeView view, @Nonnull DateIsBeforePresenterFactory beforeFactory, @Nonnull DateIsAfterPresenterFactory afterFactory) {
        super(view);
        this.beforeFactory = checkNotNull(beforeFactory);
        this.afterFactory = checkNotNull(afterFactory);
    }

    @Override
    protected void start(@Nonnull PresenterFactoryRegistry<AnnotationValueCriteria> factoryRegistry) {
        factoryRegistry.addPresenter(beforeFactory);
        factoryRegistry.addPresenter(afterFactory);
    }

    @Nonnull
    @Override
    protected CriteriaPresenterFactory<? extends AnnotationValueCriteria> getPresenterFactoryForCriteria(@Nonnull AnnotationValueCriteria criteria) {
        if(criteria instanceof DateIsBeforeCriteria) {
            return beforeFactory;
        }
        else {
            return afterFactory;
        }
    }
}
