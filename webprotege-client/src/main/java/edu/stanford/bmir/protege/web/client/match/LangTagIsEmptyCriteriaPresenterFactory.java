package edu.stanford.bmir.protege.web.client.match;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class LangTagIsEmptyCriteriaPresenterFactory implements CriteriaPresenterFactory {

    @Nonnull
    private final Provider<LangTagIsEmptyCriteriaPresenter> presenterProvider;

    @Inject
    public LangTagIsEmptyCriteriaPresenterFactory(@Nonnull Provider<LangTagIsEmptyCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "lang tag is empty";
    }

    @Nonnull
    @Override
    public CriteriaPresenter createPresenter() {
        return presenterProvider.get();
    }
}
