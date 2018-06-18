package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.StringEndsWithCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class StringEndsWithCriteriaPresenterFactory implements CriteriaPresenterFactory<StringEndsWithCriteria> {

    @Nonnull
    private final Provider<StringEndsWithCriteriaPresenter> presenterProvider;

    @Inject
    public StringEndsWithCriteriaPresenterFactory(@Nonnull Provider<StringEndsWithCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "ends with";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<StringEndsWithCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
