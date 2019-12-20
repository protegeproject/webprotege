package edu.stanford.bmir.protege.web.client.viz;

import edu.stanford.bmir.protege.web.client.match.CriteriaPresenter;
import edu.stanford.bmir.protege.web.client.match.CriteriaPresenterFactory;
import edu.stanford.bmir.protege.web.shared.viz.AnyEdgeCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-09
 */
public class AnyEdgeCriteriaPresenterFactory implements CriteriaPresenterFactory<AnyEdgeCriteria> {

    @Nonnull
    private final Provider<BlankEdgeCriteriaPresenter<AnyEdgeCriteria>> presenterProvider;

    @Inject
    public AnyEdgeCriteriaPresenterFactory(@Nonnull Provider<BlankEdgeCriteriaPresenter<AnyEdgeCriteria>> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Any edge";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends AnyEdgeCriteria> createPresenter() {
        BlankEdgeCriteriaPresenter<AnyEdgeCriteria> presenter = presenterProvider.get();
        presenter.setCriteria(AnyEdgeCriteria.get());
        return presenter;
    }
}
