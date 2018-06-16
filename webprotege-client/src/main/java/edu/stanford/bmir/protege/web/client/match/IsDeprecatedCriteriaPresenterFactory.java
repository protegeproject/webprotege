package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.RootCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class IsDeprecatedCriteriaPresenterFactory implements CriteriaPresenterFactory<EntityMatchCriteria> {

    @Nonnull
    private final Provider<IsDeprecatedCriteriaPresenter> presenterProvider;

    @Inject
    public IsDeprecatedCriteriaPresenterFactory(@Nonnull Provider<IsDeprecatedCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Is deprecated";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<EntityMatchCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
