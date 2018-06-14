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
public class DateTimeValueIsAfterPresenterFactory implements CriteriaPresenterFactory {

    @Nonnull
    private final Provider<DateTimeValueIsAfterPresenter> presenterProvider;

    @Inject
    public DateTimeValueIsAfterPresenterFactory(@Nonnull Provider<DateTimeValueIsAfterPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "after";
    }

    @Nonnull
    @Override
    public CriteriaPresenter createPresenter() {
        return presenterProvider.get();
    }
}
