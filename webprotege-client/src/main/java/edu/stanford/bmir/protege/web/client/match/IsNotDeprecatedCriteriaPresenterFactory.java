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
public class IsNotDeprecatedCriteriaPresenterFactory implements CriteriaPresenterFactory {

    @Nonnull
    private final Provider<IsNotDeprecatedCriteriaPresenter> presenterProvider;

    @Inject
    public IsNotDeprecatedCriteriaPresenterFactory(@Nonnull Provider<IsNotDeprecatedCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Is Not Deprecated";
    }

    @Nonnull
    @Override
    public CriteriaPresenter createPresenter() {
        return presenterProvider.get();
    }
}
