package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.StringEqualsCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class StringEqualsCriteriaPresenterFactory implements CriteriaPresenterFactory<StringEqualsCriteria> {

    @Nonnull
    private final Provider<StringEqualsCriteriaPresenter> presenterProvider;

    @Inject
    public StringEqualsCriteriaPresenterFactory(@Nonnull Provider<StringEqualsCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "is equal to";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<StringEqualsCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
