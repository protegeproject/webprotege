package edu.stanford.bmir.protege.web.client.match;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class StringHasUntrimmedSpaceCriteriaPresenterFactory implements CriteriaPresenterFactory {

    @Nonnull
    private final Provider<StringHasUntrimmedSpaceCriteriaPresenter> presenterProvider;

    @Inject
    public StringHasUntrimmedSpaceCriteriaPresenterFactory(@Nonnull Provider<StringHasUntrimmedSpaceCriteriaPresenter> presenterProvider) {
        this.presenterProvider = presenterProvider;
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "has untrimmed space";
    }

    @Nonnull
    @Override
    public CriteriaPresenter createPresenter() {
        return presenterProvider.get();
    }
}
