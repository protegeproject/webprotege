package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.AnyRelationshipValueCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.RelationshipValueCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.RelationshipValueMatchesCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-23
 */
public class RelationshipValueCriteriaPresenterFactory implements CriteriaPresenterFactory<RelationshipValueCriteria> {


    @Nonnull
    private final Provider<RelationshipValueCriteriaPresenter> presenterProvider;

    @Inject
    public RelationshipValueCriteriaPresenterFactory(@Nonnull Provider<RelationshipValueCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Relationship Value";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends RelationshipValueCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
