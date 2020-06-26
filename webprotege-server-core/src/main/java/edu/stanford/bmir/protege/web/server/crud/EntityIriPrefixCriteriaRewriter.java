package edu.stanford.bmir.protege.web.server.crud;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-09
 */
public class EntityIriPrefixCriteriaRewriter {

    private static final HierarchyPositionCriteriaRewriterVisitor rewriter = new HierarchyPositionCriteriaRewriterVisitor();


    @Inject
    public EntityIriPrefixCriteriaRewriter() {

    }

    /**
     * Rewrites the specified criteria that is specified in terms of the position of
     * a fresh entity into criteria that will match the specified parents of a fresh
     * entity.  The reason for doing this is that since fresh entity axioms aren't contained
     * in the ontology, criteria for matching of the fresh entity will never match to true.
     * @param criteria Criteria in terms of a fresh entity match
     * @return Criteria that match the fresh entity parents.
     */
    @Nonnull
    public RootCriteria rewriteCriteria(@Nonnull HierarchyPositionCriteria criteria) {
        return criteria.accept(rewriter);
    }


    private static class HierarchyPositionCriteriaRewriterVisitor implements HierarchyPositionCriteriaVisitor<RootCriteria> {

        @Override
        public RootCriteria visit(CompositeHierarchyPositionCriteria criteria) {
            var rewrittenCriteria = criteria.getCriteria()
                    .stream()
                    .map(c -> c.accept(this))
                    .collect(toImmutableList());
            return CompositeRootCriteria.get(rewrittenCriteria, criteria.getMatchType());
        }

        @Override
        public RootCriteria visit(SubClassOfCriteria subClassOfCriteria) {
            var targetEntity = subClassOfCriteria.getTarget();
            var entityIsCriteria = EntityIsCriteria.get(targetEntity);
            var combinedCriteria = ImmutableList.<RootCriteria>of(entityIsCriteria, subClassOfCriteria);
            return CompositeRootCriteria.get(combinedCriteria,
                                             MultiMatchType.ANY);
        }

        @Override
        public RootCriteria visit(InstanceOfCriteria instanceOfCriteria) {
            var subClassOfCriteria = SubClassOfCriteria.get(
                    instanceOfCriteria.getTarget(),
                    instanceOfCriteria.getFilterType()
            );
            return subClassOfCriteria.accept(this);
        }
    }
}
