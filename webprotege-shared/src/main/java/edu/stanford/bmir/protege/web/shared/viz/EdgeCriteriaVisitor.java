package edu.stanford.bmir.protege.web.shared.viz;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-05
 */
public interface EdgeCriteriaVisitor<R> {

    R visit(@Nonnull CompositeEdgeCriteria compositeEdgeCriteria);

    R visit(@Nonnull IncludeAnyRelationshipCriteria includeAnyRelationshipCriteria);

    R visit(@Nonnull IncludeInstanceOfCriteria includeInstanceOfCriteria);

    R visit(@Nonnull IncludePropertyCriteria includePropertyCriteria);

    R visit(@Nonnull IncludeSubClassOfCriteria includeSubClassOfCriteria);

    R visit(IncludeAnyEdgeCriteria includeAnyEdgeCriteria);
}
