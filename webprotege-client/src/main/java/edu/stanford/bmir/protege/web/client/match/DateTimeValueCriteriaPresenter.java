package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class DateTimeValueCriteriaPresenter extends SelectableCriteriaTypePresenter {

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
    protected void start(@Nonnull PresenterFactoryRegistry factoryRegistry) {
        factoryRegistry.addPresenter(beforeFactory);
        factoryRegistry.addPresenter(afterFactory);
    }
}
