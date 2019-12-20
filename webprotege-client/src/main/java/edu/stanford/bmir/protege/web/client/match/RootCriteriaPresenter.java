package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
public class RootCriteriaPresenter extends SelectableCriteriaTypePresenter<EntityMatchCriteria> {

    @Nonnull
    private final EntityAnnotationCriteriaPresenterFactory annotationCriteriaFactory;

    @Nonnull
    private final EntityAnnotationCriteriaAbsentPresenterFactory absentAnnotationCriteriaFactory;

    @Nonnull
    private final EntityAnnotationMatchesAtMostOneCriteriaPresenterFactory atMostOneAnnotationFactory;

    @Nonnull
    private final EntityTypeCriteriaPresenterFactory entityTypeFactory;

    @Nonnull
    private final IsNotBuiltInEntityPresenterFactory notBuiltInEntityFactory;

    @Nonnull
    private final IsDeprecatedCriteriaPresenterFactory isDeprecatedFactory;

    @Nonnull
    private final IsNotDeprecatedCriteriaPresenterFactory notDeprecatedFactory;

    @Nonnull
    private final NonUniqueLangTagsCriteriaPresenterFactory nonUniqueLangTags;

    @Nonnull
    private final EntityAnnotationValuesAreNotDisjointCriteriaPresenterFactory notDisjointFactory;

    @Nonnull
    private final SubClassOfCriteriaPresenterFactory subClassOfFactory;

    @Nonnull
    private final InstanceOfCriteriaPresenterFactory instanceOfFactory;

    @Nonnull
    private final EntityRelationshipPresenterFactory entityRelationshipFactory;

    @Nonnull
    private final EntityRelationshipAbsentPresenterFactory entityRelationshipAbsentFactory;

    @Nonnull
    private final EntityHasMoreThanOneRelationshipCriteriaPresenterFactory hasAtMostOneRelationshipFactory;

    private EntityIsCriteriaPresenterFactory entityIsFactory;

    @Inject
    public RootCriteriaPresenter(@Nonnull SelectableCriteriaTypeView view,
                                 @Nonnull EntityAnnotationCriteriaPresenterFactory annotationCriteriaFactory,
                                 @Nonnull EntityAnnotationCriteriaAbsentPresenterFactory absentAnnotationCriteriaFactory,
                                 @Nonnull EntityAnnotationMatchesAtMostOneCriteriaPresenterFactory atMostOneAnnotationFactory,
                                 @Nonnull EntityTypeCriteriaPresenterFactory entityTypeFactory,
                                 @Nonnull IsNotBuiltInEntityPresenterFactory notBuiltInEntityFactory,
                                 @Nonnull IsDeprecatedCriteriaPresenterFactory isDeprecatedFactory,
                                 @Nonnull IsNotDeprecatedCriteriaPresenterFactory notDeprecatedFactory,
                                 @Nonnull NonUniqueLangTagsCriteriaPresenterFactory nonUniqueLangTags,
                                 @Nonnull EntityAnnotationValuesAreNotDisjointCriteriaPresenterFactory notDisjointFactory,
                                 @Nonnull SubClassOfCriteriaPresenterFactory subClassOfFactory,
                                 @Nonnull InstanceOfCriteriaPresenterFactory instanceOfFactory,
                                 @Nonnull EntityRelationshipPresenterFactory entityRelationshipFactory,
                                 @Nonnull EntityRelationshipAbsentPresenterFactory entityRelationshipAbsentPresenterFactory,
                                 @Nonnull EntityHasMoreThanOneRelationshipCriteriaPresenterFactory hasAtMostOneRelationshipFactory,
                                 EntityIsCriteriaPresenterFactory entityIsFactory) {
        super(view);
        this.annotationCriteriaFactory = checkNotNull(annotationCriteriaFactory);
        this.absentAnnotationCriteriaFactory = checkNotNull(absentAnnotationCriteriaFactory);
        this.atMostOneAnnotationFactory = checkNotNull(atMostOneAnnotationFactory);
        this.entityTypeFactory = checkNotNull(entityTypeFactory);
        this.notBuiltInEntityFactory = checkNotNull(notBuiltInEntityFactory);
        this.isDeprecatedFactory = checkNotNull(isDeprecatedFactory);
        this.notDeprecatedFactory = checkNotNull(notDeprecatedFactory);
        this.nonUniqueLangTags = checkNotNull(nonUniqueLangTags);
        this.notDisjointFactory = checkNotNull(notDisjointFactory);
        this.subClassOfFactory = checkNotNull(subClassOfFactory);
        this.instanceOfFactory = checkNotNull(instanceOfFactory);
        this.entityRelationshipFactory = checkNotNull(entityRelationshipFactory);
        this.entityRelationshipAbsentFactory = checkNotNull(entityRelationshipAbsentPresenterFactory);
        this.hasAtMostOneRelationshipFactory = checkNotNull(hasAtMostOneRelationshipFactory);
        this.entityIsFactory = checkNotNull(entityIsFactory);
    }

    @Override
    protected void start(@Nonnull PresenterFactoryRegistry<EntityMatchCriteria> factoryRegistry) {
        factoryRegistry.addPresenter(annotationCriteriaFactory);
        factoryRegistry.addPresenter(absentAnnotationCriteriaFactory);
        factoryRegistry.addPresenter(atMostOneAnnotationFactory);
        factoryRegistry.addPresenter(entityRelationshipFactory);
        factoryRegistry.addPresenter(entityRelationshipAbsentFactory);
        factoryRegistry.addPresenter(hasAtMostOneRelationshipFactory);
        factoryRegistry.addPresenter(entityTypeFactory);
        factoryRegistry.addPresenter(entityIsFactory);
        factoryRegistry.addPresenter(isDeprecatedFactory);
        factoryRegistry.addPresenter(notDeprecatedFactory);
        factoryRegistry.addPresenter(notBuiltInEntityFactory);
        factoryRegistry.addPresenter(nonUniqueLangTags);
        factoryRegistry.addPresenter(notDisjointFactory);
        factoryRegistry.addPresenter(subClassOfFactory);
        factoryRegistry.addPresenter(instanceOfFactory);
    }

    @Nonnull
    @Override
    protected CriteriaPresenterFactory<? extends EntityMatchCriteria> getPresenterFactoryForCriteria(@Nonnull EntityMatchCriteria criteria) {
        GWT.log("[RootCriteriaPresenter] Selecting presenter factory for " + criteria);
        return criteria.accept(new RootCriteriaVisitor<CriteriaPresenterFactory<? extends EntityMatchCriteria>>() {
            @Nonnull
            @Override
            public CriteriaPresenterFactory<? extends EntityMatchCriteria> visit(@Nonnull CompositeRootCriteria criteria) {
                throw new RuntimeException("Cannot set criteria. Not supported by this presenter.");
            }

            @Nonnull
            @Override
            public CriteriaPresenterFactory<? extends EntityMatchCriteria> visit(@Nonnull EntityAnnotationCriteria criteria) {
                switch (criteria.getAnnotationPresence()) {
                    case AT_LEAST_ONE:
                        return annotationCriteriaFactory;
                    case AT_MOST_ONE:
                        return atMostOneAnnotationFactory;
                    case NONE:
                        return absentAnnotationCriteriaFactory;
                    default:
                        throw new RuntimeException("Unknown MultiType");
                }
            }

            @Nonnull
            @Override
            public CriteriaPresenterFactory<? extends EntityMatchCriteria> visit(@Nonnull EntityIsDeprecatedCriteria criteria) {
                return isDeprecatedFactory;
            }

            @Nonnull
            @Override
            public CriteriaPresenterFactory<? extends EntityMatchCriteria> visit(@Nonnull EntityIsNotDeprecatedCriteria criteria) {
                return notDeprecatedFactory;
            }

            @Nonnull
            @Override
            public CriteriaPresenterFactory<? extends EntityMatchCriteria> visit(@Nonnull EntityHasNonUniqueLangTagsCriteria criteria) {
                return nonUniqueLangTags;
            }

            @Nonnull
            @Override
            public CriteriaPresenterFactory<? extends EntityMatchCriteria> visit(@Nonnull EntityTypeIsOneOfCriteria criteria) {
                return entityTypeFactory;
            }

            @Nonnull
            @Override
            public CriteriaPresenterFactory<? extends EntityMatchCriteria> visit(@Nonnull EntityHasConflictingBooleanAnnotationValuesCriteria criteria) {
                throw new RuntimeException("Cannot set criteria. Not supported by this presenter.");
            }

            @Nonnull
            @Override
            public CriteriaPresenterFactory<? extends EntityMatchCriteria> visit(@Nonnull EntityAnnotationValuesAreNotDisjointCriteria criteria) {
                return notDisjointFactory;
            }

            @Nonnull
            @Override
            public CriteriaPresenterFactory<? extends EntityMatchCriteria> visit(@Nonnull IsNotBuiltInEntityCriteria criteria) {
                return notBuiltInEntityFactory;
            }

            @Nonnull
            @Override
            public CriteriaPresenterFactory<? extends EntityMatchCriteria> visit(@Nonnull SubClassOfCriteria criteria) {
                return subClassOfFactory;
            }

            @Nonnull
            @Override
            public CriteriaPresenterFactory<? extends EntityMatchCriteria> visit(@Nonnull InstanceOfCriteria instanceOfCriteria) {
                return instanceOfFactory;
            }

            @Nonnull
            @Override
            public CriteriaPresenterFactory<? extends EntityMatchCriteria> visit(@Nonnull EntityRelationshipCriteria criteria) {
                switch (criteria.getRelationshipPresence()) {
                    case AT_LEAST_ONE:
                        return entityRelationshipFactory;
                    case AT_MOST_ONE:
                        return hasAtMostOneRelationshipFactory;
                    case NONE:
                        return entityRelationshipAbsentFactory;
                    default:
                        throw new RuntimeException("Unknown MultiType");
                }
            }

            @Nonnull
            @Override
            public CriteriaPresenterFactory<? extends EntityMatchCriteria> visit(EntityIsCriteria entityIsCriteria) {
                return entityIsFactory;
            }
        });
    }
}
