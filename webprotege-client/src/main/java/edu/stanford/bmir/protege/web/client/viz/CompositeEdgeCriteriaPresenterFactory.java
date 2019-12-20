package edu.stanford.bmir.protege.web.client.viz;

import edu.stanford.bmir.protege.web.client.match.CriteriaPresenter;
import edu.stanford.bmir.protege.web.client.match.CriteriaPresenterFactory;
import edu.stanford.bmir.protege.web.shared.viz.CompositeEdgeCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public class CompositeEdgeCriteriaPresenterFactory implements CriteriaPresenterFactory<CompositeEdgeCriteria> {

    @Nonnull
    private final Provider<CompositeEdgeCriteriaPresenter> edgeCriteriaPresenterProvider;

    @Inject
    public CompositeEdgeCriteriaPresenterFactory(@Nonnull Provider<CompositeEdgeCriteriaPresenter> edgeCriteriaPresenterProvider) {
        this.edgeCriteriaPresenterProvider = edgeCriteriaPresenterProvider;
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Edge matches";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends CompositeEdgeCriteria> createPresenter() {
        return edgeCriteriaPresenterProvider.get();
    }
}
