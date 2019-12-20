package edu.stanford.bmir.protege.web.shared.viz;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-05
 */
public interface EdgeCriteriaVisitor<R> {

    R visit(@Nonnull CompositeEdgeCriteria criteria);

    R visit(@Nonnull AnyRelationshipEdgeCriteria criteria);

    R visit(@Nonnull AnyInstanceOfEdgeCriteria criteria);

    R visit(@Nonnull RelationshipEdgePropertyEqualsCriteria criteria);

    R visit(@Nonnull AnySubClassOfEdgeCriteria criteria);

    R visit(@Nonnull AnyEdgeCriteria criteria);

    R visit(@Nonnull TailNodeMatchesCriteria criteria);

    R visit(@Nonnull HeadNodeMatchesCriteria criteria);

    R visit(@Nonnull AnyNodeCriteria criteria);

    R visit(@Nonnull NegatedEdgeCriteria criteria);

    R visit(@Nonnull NoEdgeCriteria noEdgeCriteria);
}
