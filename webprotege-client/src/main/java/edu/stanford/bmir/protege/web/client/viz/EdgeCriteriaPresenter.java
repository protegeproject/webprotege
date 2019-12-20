package edu.stanford.bmir.protege.web.client.viz;

import edu.stanford.bmir.protege.web.client.match.CriteriaPresenterFactory;
import edu.stanford.bmir.protege.web.client.match.SelectableCriteriaTypePresenter;
import edu.stanford.bmir.protege.web.client.match.SelectableCriteriaTypeView;
import edu.stanford.bmir.protege.web.shared.viz.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public class EdgeCriteriaPresenter extends SelectableCriteriaTypePresenter<EdgeCriteria> {

    @Nonnull
    private final RelationshipEdgePropertyEqualsCriteriaPresenterFactory propertyEqualsCriteriaPresenterFactory;

    @Nonnull
    private final HeadNodeMatchesCriteriaPresenterFactory headNodeMatchesCriteriaPresenterFactory;

    @Nonnull
    private final TailNodeMatchesCriteriaPresenterFactory tailNodeMatchesCriteriaPresenterFactory;

    @Nonnull
    private final AnyRelationshipEdgeCriteriaPresenterFactory anyRelationshipEdgeCriteriaPresenterFactory;

    @Nonnull
    private final AnyInstanceOfEdgeCriteriaPresenterFactory anyInstanceOfEdgeCriteriaPresenterFactory;

    @Nonnull
    private final AnySubClassofEdgeCriteriaPresenterFactory anySubClassofEdgeCriteriaPresenterFactory;

    @Nonnull
    private final CompositeEdgeCriteriaPresenterFactory compositeEdgeCriteriaPresenterFactory;

    @Nonnull
    private final AnyEdgeCriteriaPresenterFactory anyEdgeCriteriaPresenterFactory;

    @Nonnull
    private final NoEdgeCriteriaPresenterFactory noEdgeCriteriaPresenterFactory;

    @Inject
    public EdgeCriteriaPresenter(@Nonnull SelectableCriteriaTypeView view,
                                 @Nonnull RelationshipEdgePropertyEqualsCriteriaPresenterFactory propertyEqualsCriteriaPresenterFactory,
                                 @Nonnull HeadNodeMatchesCriteriaPresenterFactory headNodeMatchesCriteriaPresenterFactory,
                                 @Nonnull TailNodeMatchesCriteriaPresenterFactory tailNodeMatchesCriteriaPresenterFactory,
                                 @Nonnull AnyRelationshipEdgeCriteriaPresenterFactory anyRelationshipEdgeCriteriaPresenterFactory,
                                 @Nonnull AnyInstanceOfEdgeCriteriaPresenterFactory anyInstanceOfEdgeCriteriaPresenterFactory,
                                 @Nonnull AnySubClassofEdgeCriteriaPresenterFactory anySubClassofEdgeCriteriaPresenterFactory,
                                 @Nonnull CompositeEdgeCriteriaPresenterFactory compositeEdgeCriteriaPresenterFactory,
                                 @Nonnull AnyEdgeCriteriaPresenterFactory anyEdgeCriteriaPresenterFactory,
                                 @Nonnull NoEdgeCriteriaPresenterFactory noEdgeCriteriaPresenterFactory) {
        super(view);
        this.propertyEqualsCriteriaPresenterFactory = propertyEqualsCriteriaPresenterFactory;
        this.headNodeMatchesCriteriaPresenterFactory = headNodeMatchesCriteriaPresenterFactory;
        this.tailNodeMatchesCriteriaPresenterFactory = tailNodeMatchesCriteriaPresenterFactory;
        this.anyRelationshipEdgeCriteriaPresenterFactory = anyRelationshipEdgeCriteriaPresenterFactory;
        this.anyInstanceOfEdgeCriteriaPresenterFactory = anyInstanceOfEdgeCriteriaPresenterFactory;
        this.anySubClassofEdgeCriteriaPresenterFactory = anySubClassofEdgeCriteriaPresenterFactory;
        this.compositeEdgeCriteriaPresenterFactory = compositeEdgeCriteriaPresenterFactory;
        this.anyEdgeCriteriaPresenterFactory = anyEdgeCriteriaPresenterFactory;
        this.noEdgeCriteriaPresenterFactory = noEdgeCriteriaPresenterFactory;
    }

    @Override
    protected void start(@Nonnull PresenterFactoryRegistry<EdgeCriteria> factoryRegistry) {
        factoryRegistry.addPresenter(anyEdgeCriteriaPresenterFactory);
        factoryRegistry.addPresenter(anySubClassofEdgeCriteriaPresenterFactory);
        factoryRegistry.addPresenter(anyInstanceOfEdgeCriteriaPresenterFactory);
        factoryRegistry.addPresenter(anyRelationshipEdgeCriteriaPresenterFactory);
        factoryRegistry.addPresenter(propertyEqualsCriteriaPresenterFactory);
        factoryRegistry.addPresenter(headNodeMatchesCriteriaPresenterFactory);
        factoryRegistry.addPresenter(tailNodeMatchesCriteriaPresenterFactory);
        factoryRegistry.addPresenter(compositeEdgeCriteriaPresenterFactory);
        factoryRegistry.addPresenter(noEdgeCriteriaPresenterFactory);
    }

    @Nonnull
    @Override
    protected CriteriaPresenterFactory<? extends EdgeCriteria> getPresenterFactoryForCriteria(@Nonnull EdgeCriteria criteria) {
        return criteria.accept(new EdgeCriteriaVisitor<CriteriaPresenterFactory<? extends EdgeCriteria>>() {
            @Override
            public CriteriaPresenterFactory<? extends EdgeCriteria> visit(@Nonnull CompositeEdgeCriteria criteria) {
                return compositeEdgeCriteriaPresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends EdgeCriteria> visit(@Nonnull AnyRelationshipEdgeCriteria criteria) {
                return anyRelationshipEdgeCriteriaPresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends EdgeCriteria> visit(@Nonnull AnyInstanceOfEdgeCriteria criteria) {
                return anyInstanceOfEdgeCriteriaPresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends EdgeCriteria> visit(@Nonnull RelationshipEdgePropertyEqualsCriteria criteria) {
                return propertyEqualsCriteriaPresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends EdgeCriteria> visit(@Nonnull AnySubClassOfEdgeCriteria criteria) {
                return anySubClassofEdgeCriteriaPresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends EdgeCriteria> visit(@Nonnull AnyEdgeCriteria criteria) {
                return anyEdgeCriteriaPresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends EdgeCriteria> visit(@Nonnull TailNodeMatchesCriteria criteria) {
                return tailNodeMatchesCriteriaPresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends EdgeCriteria> visit(@Nonnull HeadNodeMatchesCriteria criteria) {
                return headNodeMatchesCriteriaPresenterFactory;
            }

            @Override
            public CriteriaPresenterFactory<? extends EdgeCriteria> visit(@Nonnull AnyNodeCriteria criteria) {
                return null;
            }

            @Override
            public CriteriaPresenterFactory<? extends EdgeCriteria> visit(@Nonnull NegatedEdgeCriteria criteria) {
                return null;
            }

            @Override
            public CriteriaPresenterFactory<? extends EdgeCriteria> visit(@Nonnull NoEdgeCriteria noEdgeCriteria) {
                return noEdgeCriteriaPresenterFactory;
            }
        });
    }
}
