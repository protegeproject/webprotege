package edu.stanford.bmir.protege.web.client.viz;

import edu.stanford.bmir.protege.web.client.match.CriteriaPresenter;
import edu.stanford.bmir.protege.web.client.match.CriteriaPresenterFactory;
import edu.stanford.bmir.protege.web.shared.viz.EdgeCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public class RootEdgeCriteriaPresenterFactory implements CriteriaPresenterFactory<EdgeCriteria> {

    @Nonnull
    private final Provider<EdgeCriteriaPresenter> presenterProvider;

    @Inject
    public RootEdgeCriteriaPresenterFactory(@Nonnull Provider<EdgeCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Root Edge Criteria";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends EdgeCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
