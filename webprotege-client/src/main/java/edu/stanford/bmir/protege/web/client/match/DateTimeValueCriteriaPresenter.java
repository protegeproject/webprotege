package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class DateTimeValueCriteriaPresenter extends SelectableCriteriaTypePresenter<LiteralCriteria> {

    @Nonnull
    private final DateTimeValueIsBeforePresenterFactory beforeFactory;

    @Nonnull
    private final DateTimeValueIsAfterPresenterFactory afterFactory;

    @Inject
    public DateTimeValueCriteriaPresenter(@Nonnull SelectableCriteriaTypeView view, @Nonnull DateTimeValueIsBeforePresenterFactory beforeFactory, @Nonnull DateTimeValueIsAfterPresenterFactory afterFactory) {
        super(view);
        this.beforeFactory = checkNotNull(beforeFactory);
        this.afterFactory = checkNotNull(afterFactory);
    }

    @Override
    protected void start(@Nonnull PresenterFactoryRegistry<LiteralCriteria> factoryRegistry) {
        factoryRegistry.addPresenter(beforeFactory);
        factoryRegistry.addPresenter(afterFactory);
    }
}
