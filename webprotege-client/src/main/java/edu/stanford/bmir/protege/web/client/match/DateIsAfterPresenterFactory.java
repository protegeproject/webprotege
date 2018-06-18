package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.DateIsAfterCriteria;
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
public class DateIsAfterPresenterFactory implements CriteriaPresenterFactory<DateIsAfterCriteria> {

    @Nonnull
    private final Provider<DateIsAfterPresenter> presenterProvider;

    @Inject
    public DateIsAfterPresenterFactory(@Nonnull Provider<DateIsAfterPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "after";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<DateIsAfterCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
