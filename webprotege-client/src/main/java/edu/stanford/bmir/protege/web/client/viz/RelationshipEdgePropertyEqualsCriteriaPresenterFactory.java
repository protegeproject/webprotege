package edu.stanford.bmir.protege.web.client.viz;

import edu.stanford.bmir.protege.web.client.match.CriteriaPresenter;
import edu.stanford.bmir.protege.web.client.match.CriteriaPresenterFactory;
import edu.stanford.bmir.protege.web.shared.viz.RelationshipEdgePropertyEqualsCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public class RelationshipEdgePropertyEqualsCriteriaPresenterFactory implements CriteriaPresenterFactory<RelationshipEdgePropertyEqualsCriteria> {

    @Nonnull
    private final Provider<RelationshipEdgePropertyEqualsCriteriaPresenter> presenterProvider;

    @Inject
    public RelationshipEdgePropertyEqualsCriteriaPresenterFactory(@Nonnull Provider<RelationshipEdgePropertyEqualsCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Edge property is";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends RelationshipEdgePropertyEqualsCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
