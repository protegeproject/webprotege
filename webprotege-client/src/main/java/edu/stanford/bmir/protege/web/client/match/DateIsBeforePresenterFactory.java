package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.DateIsBeforeCriteria;
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
public class DateIsBeforePresenterFactory implements CriteriaPresenterFactory<DateIsBeforeCriteria> {

    @Nonnull
    private final Provider<DateIsBeforePresenter> presenterProvider;

    @Inject
    public DateIsBeforePresenterFactory(@Nonnull Provider<DateIsBeforePresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "before";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<DateIsBeforeCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
