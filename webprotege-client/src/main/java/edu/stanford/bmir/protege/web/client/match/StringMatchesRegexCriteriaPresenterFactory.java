package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.StringContainsRegexMatchCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class StringMatchesRegexCriteriaPresenterFactory implements CriteriaPresenterFactory<StringContainsRegexMatchCriteria> {

    @Nonnull
    private final Provider<StringMatchesRegexCriteriaPresenter> presenterProvider;

    @Inject
    public StringMatchesRegexCriteriaPresenterFactory(@Nonnull Provider<StringMatchesRegexCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "matches regex";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<StringContainsRegexMatchCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
