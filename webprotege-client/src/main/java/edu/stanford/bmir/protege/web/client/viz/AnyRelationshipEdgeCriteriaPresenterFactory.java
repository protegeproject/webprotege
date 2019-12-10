package edu.stanford.bmir.protege.web.client.viz;

import edu.stanford.bmir.protege.web.client.match.CriteriaPresenter;
import edu.stanford.bmir.protege.web.client.match.CriteriaPresenterFactory;
import edu.stanford.bmir.protege.web.shared.viz.AnyInstanceOfEdgeCriteria;
import edu.stanford.bmir.protege.web.shared.viz.AnyRelationshipEdgeCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public class AnyRelationshipEdgeCriteriaPresenterFactory implements CriteriaPresenterFactory<AnyRelationshipEdgeCriteria> {

    @Nonnull
    private final Provider<BlankEdgeCriteriaPresenter<AnyRelationshipEdgeCriteria>> presenterProvider;

    @Inject
    public AnyRelationshipEdgeCriteriaPresenterFactory(@Nonnull Provider<BlankEdgeCriteriaPresenter<AnyRelationshipEdgeCriteria>> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Edge is a property";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends AnyRelationshipEdgeCriteria> createPresenter() {
        BlankEdgeCriteriaPresenter<AnyRelationshipEdgeCriteria> presenter = presenterProvider.get();
        presenter.setCriteria(AnyRelationshipEdgeCriteria.get());
        return presenter;
    }
}
