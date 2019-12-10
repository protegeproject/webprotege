package edu.stanford.bmir.protege.web.client.viz;

import edu.stanford.bmir.protege.web.client.match.CriteriaPresenter;
import edu.stanford.bmir.protege.web.client.match.CriteriaPresenterFactory;
import edu.stanford.bmir.protege.web.shared.viz.HeadNodeMatchesCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public class HeadNodeMatchesCriteriaPresenterFactory implements CriteriaPresenterFactory<HeadNodeMatchesCriteria> {

    @Nonnull
    private final Provider<HeadNodeMatchesCriteriaPresenter> presenterProvider;

    @Inject
    public HeadNodeMatchesCriteriaPresenterFactory(@Nonnull Provider<HeadNodeMatchesCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Head node entity";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends HeadNodeMatchesCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
