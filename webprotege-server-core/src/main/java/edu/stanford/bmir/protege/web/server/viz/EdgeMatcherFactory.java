package edu.stanford.bmir.protege.web.server.viz;


import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.match.MatcherFactory;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
import edu.stanford.bmir.protege.web.shared.viz.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static dagger.internal.codegen.DaggerStreams.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-05
 */
public class EdgeMatcherFactory {

    @Nonnull
    private final MatcherFactory matcherFactory;

    @Inject
    public EdgeMatcherFactory(@Nonnull MatcherFactory matcherFactory) {
        this.matcherFactory = matcherFactory;
    }

    @Nonnull
    public EdgeMatcher createMatcher(@Nonnull EdgeCriteria criteria) {
        EdgeCriteriaVisitor<EdgeMatcher> mapper = new EdgeCriteriaVisitor<>() {
            @Override
            public EdgeMatcher visit(@Nonnull CompositeEdgeCriteria compositeEdgeCriteria) {
                ImmutableList<EdgeMatcher> matchers = compositeEdgeCriteria.getCriteria()
                                                                          .stream()
                                                                          .map(EdgeMatcherFactory.this::createMatcher)
                                                                          .collect(toImmutableList());
                return new CompositeEdgeMatcher(matchers,
                                                compositeEdgeCriteria.getMatchType());
            }

            @Override
            public EdgeMatcher visit(@Nonnull AnyRelationshipEdgeCriteria includeAnyPropertyCriteria) {
                return Edge::isRelationship;
            }

            @Override
            public EdgeMatcher visit(@Nonnull AnyInstanceOfEdgeCriteria includeInstanceOfCriteria) {
                return edge -> edge.isIsA() && edge.getTail().getEntity().isIndividual();
            }

            @Override
            public EdgeMatcher visit(@Nonnull RelationshipEdgePropertyEqualsCriteria includePropertyCriteria) {
                return edge -> edge.isRelationship() && edge.getLabellingEntity().filter(rel -> includePropertyCriteria.getProperty().equals(rel.getEntity())).isPresent();
            }

            @Override
            public EdgeMatcher visit(@Nonnull AnySubClassOfEdgeCriteria includeSubClassOfCriteria) {
                return edge -> edge.isIsA() && edge.getTail().getEntity().isOWLClass();
            }

            @Override
            public EdgeMatcher visit(@Nonnull AnyEdgeCriteria includeAnyEdgeCriteria) {
                return edge -> true;
            }

            @Override
            public EdgeMatcher visit(@Nonnull NoEdgeCriteria noEdgeCriteria) {
                return edge -> false;
            }

            @Override
            public EdgeMatcher visit(@Nonnull TailNodeMatchesCriteria criteria) {
                return edge -> matcherFactory.getMatcher(criteria.getNodeCriteria()).matches(edge.getTail().getEntity());
            }

            @Override
            public EdgeMatcher visit(@Nonnull HeadNodeMatchesCriteria criteria) {
                return edge -> matcherFactory.getMatcher(criteria.getNodeCriteria()).matches(edge.getHead().getEntity());
            }

            @Override
            public EdgeMatcher visit(@Nonnull AnyNodeCriteria criteria) {
                return edge -> true;
            }

            @Override
            public EdgeMatcher visit(@Nonnull NegatedEdgeCriteria criteria) {
                return new EdgeNegatedMatcher(createMatcher(criteria.getNegatedCriteria()));
            }
        };
        return criteria.accept(mapper);
    }


    private static class CompositeEdgeMatcher implements EdgeMatcher {

        @Nonnull
        private final ImmutableList<EdgeMatcher> matchers;

        @Nonnull
        private final MultiMatchType multiMatchType;

        public CompositeEdgeMatcher(@Nonnull ImmutableList<EdgeMatcher> matchers,
                                    @Nonnull MultiMatchType multiMatchType) {
            this.matchers = checkNotNull(matchers);
            this.multiMatchType = checkNotNull(multiMatchType);
        }

        @Override
        public boolean matches(@Nonnull Edge edge) {
            if(multiMatchType == MultiMatchType.ALL) {
                // NB Logical FOR ALL – allMatch is true if the stream is empty
                return matchers.stream().allMatch(m -> m.matches(edge));
            }
            else {
                // NB Logical EXISTS – anyMatch is false is stream is empty
                return matchers.stream().anyMatch(m -> m.matches(edge));
            }
        }
    }


    private static class EdgeNegatedMatcher implements EdgeMatcher {

        @Nonnull
        private final EdgeMatcher negatedMatcher;

        public EdgeNegatedMatcher(@Nonnull EdgeMatcher negatedMatcher) {
            this.negatedMatcher = negatedMatcher;
        }

        @Override
        public boolean matches(@Nonnull Edge edge) {
            return !negatedMatcher.matches(edge);
        }
    }
}
