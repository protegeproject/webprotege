package edu.stanford.bmir.protege.web.server.viz;


import com.google.common.collect.ImmutableList;
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

    @Inject
    public EdgeMatcherFactory() {
    }

    @Nonnull
    public EdgeMatcher createMatcher(@Nonnull EdgeCriteria criteria) {
        EdgeCriteriaVisitor<EdgeMatcher> mapper = new EdgeCriteriaVisitor<>() {
            @Override
            public EdgeMatcher visit(@Nonnull CompositeEdgeCriteria compositeEdgeCriteria) {
                return new CompositeEdgeMatcher(compositeEdgeCriteria.getCriteria().stream().map(EdgeMatcherFactory.this::createMatcher).collect(toImmutableList()));
            }

            @Override
            public EdgeMatcher visit(@Nonnull IncludeAnyRelationshipCriteria includeAnyPropertyCriteria) {
                return Edge::isRelationship;
            }

            @Override
            public EdgeMatcher visit(@Nonnull IncludeInstanceOfCriteria includeInstanceOfCriteria) {
                return edge -> edge.isIsA() && edge.getTail().getEntity().isIndividual();
            }

            @Override
            public EdgeMatcher visit(@Nonnull IncludePropertyCriteria includePropertyCriteria) {
                return edge -> edge.isRelationship() && edge.getLabellingEntity().filter(rel -> includePropertyCriteria.getProperty().equals(rel.getEntity())).isPresent();
            }

            @Override
            public EdgeMatcher visit(@Nonnull IncludeSubClassOfCriteria includeSubClassOfCriteria) {
                return edge -> edge.isIsA() && edge.getTail().getEntity().isOWLClass();
            }

            @Override
            public EdgeMatcher visit(IncludeAnyEdgeCriteria includeAnyEdgeCriteria) {
                return edge -> true;
            }
        };
        return criteria.accept(mapper);
    }


    private static class CompositeEdgeMatcher implements EdgeMatcher {

        @Nonnull
        private final ImmutableList<EdgeMatcher> matchers;

        public CompositeEdgeMatcher(@Nonnull ImmutableList<EdgeMatcher> matchers) {
            this.matchers = checkNotNull(matchers);
        }

        @Override
        public boolean matches(@Nonnull Edge edge) {
            return matchers.stream().anyMatch(m -> m.matches(edge));
        }
    }
}
