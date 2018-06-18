package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.LangTagMatchesCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class LangTagMatchesCriteriaPresenterFactory implements CriteriaPresenterFactory<LangTagMatchesCriteria> {

    @Nonnull
    private final Provider<LangMatchesCriteriaPresenter> presenterProvider;

    @Inject
    public LangTagMatchesCriteriaPresenterFactory(@Nonnull Provider<LangMatchesCriteriaPresenter> presenterProvider) {
        this.presenterProvider = presenterProvider;
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "has a lang that matches";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<LangTagMatchesCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
