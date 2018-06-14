package edu.stanford.bmir.protege.web.client.match;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class LangTagMatchesCriteriaPresenterFactory implements CriteriaPresenterFactory {

    @Nonnull
    private final Provider<LangMatchesCriteriaPresenter> presenterProvider;

    @Inject
    public LangTagMatchesCriteriaPresenterFactory(@Nonnull Provider<LangMatchesCriteriaPresenter> presenterProvider) {
        this.presenterProvider = presenterProvider;
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "lang tag matches";
    }

    @Nonnull
    @Override
    public CriteriaPresenter createPresenter() {
        return presenterProvider.get();
    }
}
