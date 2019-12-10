package edu.stanford.bmir.protege.web.client.viz;

import edu.stanford.bmir.protege.web.client.match.CriteriaPresenter;
import edu.stanford.bmir.protege.web.client.match.CriteriaPresenterFactory;
import edu.stanford.bmir.protege.web.shared.viz.AnySubClassOfEdgeCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public class AnySubClassofEdgeCriteriaPresenterFactory implements CriteriaPresenterFactory<AnySubClassOfEdgeCriteria> {

    @Nonnull
    private final Provider<BlankEdgeCriteriaPresenter<AnySubClassOfEdgeCriteria>> presenterProvider;

    @Inject
    public AnySubClassofEdgeCriteriaPresenterFactory(@Nonnull Provider<BlankEdgeCriteriaPresenter<AnySubClassOfEdgeCriteria>> presenterProvider) {
        this.presenterProvider = presenterProvider;
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Edge is SubClassOf";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends AnySubClassOfEdgeCriteria> createPresenter() {
        BlankEdgeCriteriaPresenter<AnySubClassOfEdgeCriteria> presenter = presenterProvider.get();
        presenter.setCriteria(AnySubClassOfEdgeCriteria.get());
        return presenter;
    }
    
}
