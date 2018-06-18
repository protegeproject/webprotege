package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.StringDoesNotContainRegexMatchCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
public class StringDoesNotMatchRegexCriteriaPresenterFactory implements CriteriaPresenterFactory<StringDoesNotContainRegexMatchCriteria> {

    @Nonnull
    private final Provider<StringDoesNotMatchRegexCriteriaPresenter> presenterProvider;

    @Inject
    public StringDoesNotMatchRegexCriteriaPresenterFactory(@Nonnull Provider<StringDoesNotMatchRegexCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "does not match regex";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<StringDoesNotContainRegexMatchCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
