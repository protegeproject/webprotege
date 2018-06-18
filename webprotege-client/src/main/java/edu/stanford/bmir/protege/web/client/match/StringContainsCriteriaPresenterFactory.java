package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.StringContainsCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class StringContainsCriteriaPresenterFactory implements CriteriaPresenterFactory<StringContainsCriteria> {

    @Nonnull
    private final Provider<StringContainsPresenter> presenterProvider;

    @Inject
    public StringContainsCriteriaPresenterFactory(@Nonnull Provider<StringContainsPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "contains";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<StringContainsCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
