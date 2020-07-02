package edu.stanford.bmir.protege.web.client.hierarchy;

import edu.stanford.bmir.protege.web.client.match.CriteriaPresenter;
import edu.stanford.bmir.protege.web.client.match.CriteriaPresenterFactory;
import edu.stanford.bmir.protege.web.shared.match.criteria.HierarchyPositionCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-08
 */
public class HierarchyPositionCriteriaPresenterFactory implements CriteriaPresenterFactory<HierarchyPositionCriteria> {

    private Provider<HierarchyPositionCriteriaPresenter> hierarchyPositionCriteriaPresenterProvider;

    @Inject
    public HierarchyPositionCriteriaPresenterFactory(Provider<HierarchyPositionCriteriaPresenter> hierarchyPositionCriteriaPresenterProvider) {
        this.hierarchyPositionCriteriaPresenterProvider = checkNotNull(hierarchyPositionCriteriaPresenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Hierarchy position matches";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends HierarchyPositionCriteria> createPresenter() {
        return hierarchyPositionCriteriaPresenterProvider.get();
    }
}
