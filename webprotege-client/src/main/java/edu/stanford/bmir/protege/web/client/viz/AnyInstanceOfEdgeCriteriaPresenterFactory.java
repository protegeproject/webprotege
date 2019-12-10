package edu.stanford.bmir.protege.web.client.viz;

import edu.stanford.bmir.protege.web.client.match.CriteriaPresenter;
import edu.stanford.bmir.protege.web.client.match.CriteriaPresenterFactory;
import edu.stanford.bmir.protege.web.shared.viz.AnyInstanceOfEdgeCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public class AnyInstanceOfEdgeCriteriaPresenterFactory implements CriteriaPresenterFactory<AnyInstanceOfEdgeCriteria> {

    @Nonnull
    private final Provider<BlankEdgeCriteriaPresenter<AnyInstanceOfEdgeCriteria>> presenterProvider;

    @Inject
    public AnyInstanceOfEdgeCriteriaPresenterFactory(@Nonnull Provider<BlankEdgeCriteriaPresenter<AnyInstanceOfEdgeCriteria>> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Edge is InstanceOf";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends AnyInstanceOfEdgeCriteria> createPresenter() {
        BlankEdgeCriteriaPresenter<AnyInstanceOfEdgeCriteria> presenter = presenterProvider.get();
        presenter.setCriteria(AnyInstanceOfEdgeCriteria.get());
        return presenter;
    }
}
