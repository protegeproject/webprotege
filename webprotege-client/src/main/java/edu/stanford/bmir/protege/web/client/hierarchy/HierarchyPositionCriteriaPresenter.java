package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.*;
import edu.stanford.bmir.protege.web.shared.match.criteria.HierarchyPositionCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.InstanceOfCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.SubClassOfCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-08
 */
public class HierarchyPositionCriteriaPresenter extends SelectableCriteriaTypePresenter<HierarchyPositionCriteria> {

    @Nonnull
    private final SubClassOfCriteriaPresenterFactory subClassOfCriteriaPresenterFactory;

    @Nonnull
    private final InstanceOfCriteriaPresenterFactory instanceOfCriteriaPresenterFactory;

    @Inject
    public HierarchyPositionCriteriaPresenter(@Nonnull SelectableCriteriaTypeView view,
                                              @Nonnull SubClassOfCriteriaPresenterFactory subClassOfCriteriaPresenterFactory,
                                              @Nonnull InstanceOfCriteriaPresenterFactory instanceOfCriteriaPresenterFactory) {
        super(view);
        this.subClassOfCriteriaPresenterFactory = checkNotNull(subClassOfCriteriaPresenterFactory);
        this.instanceOfCriteriaPresenterFactory = checkNotNull(instanceOfCriteriaPresenterFactory);
    }

    @Override
    protected void start(@Nonnull PresenterFactoryRegistry<HierarchyPositionCriteria> factoryRegistry) {
        factoryRegistry.addPresenter(subClassOfCriteriaPresenterFactory);
        factoryRegistry.addPresenter(instanceOfCriteriaPresenterFactory);
    }

    @Nonnull
    @Override
    protected CriteriaPresenterFactory<? extends HierarchyPositionCriteria> getPresenterFactoryForCriteria(@Nonnull HierarchyPositionCriteria criteria) {
        if(criteria instanceof SubClassOfCriteria) {
            return subClassOfCriteriaPresenterFactory;
        }
        else {
            return instanceOfCriteriaPresenterFactory;
        }
    }
}
