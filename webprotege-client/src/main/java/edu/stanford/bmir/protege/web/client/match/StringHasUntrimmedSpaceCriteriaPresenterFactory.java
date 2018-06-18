package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.StringHasUntrimmedSpaceCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class StringHasUntrimmedSpaceCriteriaPresenterFactory implements CriteriaPresenterFactory<StringHasUntrimmedSpaceCriteria> {

    @Nonnull
    private final Provider<StringHasUntrimmedSpaceCriteriaPresenter> presenterProvider;

    @Inject
    public StringHasUntrimmedSpaceCriteriaPresenterFactory(@Nonnull Provider<StringHasUntrimmedSpaceCriteriaPresenter> presenterProvider) {
        this.presenterProvider = presenterProvider;
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "contains untrimmed space";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<StringHasUntrimmedSpaceCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
