package edu.stanford.bmir.protege.web.client.viz;

import edu.stanford.bmir.protege.web.client.match.CriteriaPresenter;
import edu.stanford.bmir.protege.web.client.match.CriteriaPresenterFactory;
import edu.stanford.bmir.protege.web.shared.viz.NoEdgeCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-19
 */
public class NoEdgeCriteriaPresenterFactory implements CriteriaPresenterFactory<NoEdgeCriteria> {

    private final Provider<NoEdgeCriteriaPresenter> presenterProvider;

    @Inject
    public NoEdgeCriteriaPresenterFactory(Provider<NoEdgeCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "No Edge";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends NoEdgeCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
