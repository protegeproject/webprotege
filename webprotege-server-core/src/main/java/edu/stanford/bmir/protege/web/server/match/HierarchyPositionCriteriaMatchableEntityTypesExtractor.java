package edu.stanford.bmir.protege.web.server.match;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-05-11
 */
public class HierarchyPositionCriteriaMatchableEntityTypesExtractor {

    private static final CriteriaVisitor CRITERIA_VISITOR = new CriteriaVisitor();

    @Inject
    public HierarchyPositionCriteriaMatchableEntityTypesExtractor() {
    }

    /**
     * Gets the {@link EntityType}s that can be matched by the specified criteria
     * @param hierarchyPositionCriteria The prefix
     * @return A set of entity types that represent the types of entities that can be matched by the leaf matchers in
     * the criterial for the condition IRI prefix.
     */
    @Nonnull
    public ImmutableSet<EntityType<?>> getMatchableEntityTypes(@Nonnull HierarchyPositionCriteria hierarchyPositionCriteria) {
        return hierarchyPositionCriteria.accept(CRITERIA_VISITOR);
    }


    private static class CriteriaVisitor implements HierarchyPositionCriteriaVisitor<ImmutableSet<EntityType<?>>> {

        @Override
        public ImmutableSet<EntityType<?>> visit(CompositeHierarchyPositionCriteria criteria) {
            var builder = ImmutableSet.<EntityType<?>>builder();
            criteria.getCriteria()
                    .stream()
                    .map(c -> c.accept(this))
                    .forEach(builder::addAll);
            var entityTypes = builder.build();
            if(criteria.getMatchType().equals(MultiMatchType.ALL)) {
                // Conjunction.  We can only have one type for an entity
                if(entityTypes.size() == 1) {
                    return entityTypes;
                }
                else {
                    // Unsatisfiable.  An entity is only one type
                    return ImmutableSet.of();
                }
            }
            else {
                // Disjunction.  Multiple types are allowed.
                return entityTypes;
            }
        }

        @Override
        public ImmutableSet<EntityType<?>> visit(SubClassOfCriteria subClassOfCriteria) {
            return ImmutableSet.of(EntityType.CLASS);
        }

        @Override
        public ImmutableSet<EntityType<?>> visit(InstanceOfCriteria instanceOfCriteria) {
            return ImmutableSet.of(EntityType.NAMED_INDIVIDUAL);
        }

        @Override
        public ImmutableSet<EntityType<?>> visit(NotSubClassOfCriteria notSubClassOfCriteria) {
            return ImmutableSet.of(EntityType.CLASS);
        }

        @Override
        public ImmutableSet<EntityType<?>> visit(IsLeafClassCriteria isALeafClassCriteria) {
            return ImmutableSet.of(EntityType.CLASS);
        }
    }
}
