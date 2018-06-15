package edu.stanford.bmir.protege.web.shared.match.criteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
public interface AnnotationValueCriteriaVisitor<R> extends LiteralCriteriaVisitor<R> {

    @Nonnull
    R visit(@Nonnull AnyAnnotationValueCriteria criteria);

    @Nonnull
    R visit(@Nonnull IriEqualsCriteria criteria);

    @Nonnull
    R visit(@Nonnull IriHasAnnotationsCriteria criteria);

    @Nonnull
    R visit(@Nonnull CompositeAnnotationValueCriteria criteria);
}
