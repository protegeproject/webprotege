package edu.stanford.bmir.protege.web.shared.match.criteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
public interface LexicalValueCriteriaVisitor<R> {

    R visit(@Nonnull AnyStringCriteria criteria);

    R visit(@Nonnull StringStartsWithCriteria criteria);

    R visit(@Nonnull StringEndsWithCriteria criteria);

    R visit(@Nonnull StringContainsCriteria criteria);

    R visit(@Nonnull StringEqualsCriteria criteria);

    R visit(@Nonnull NumericValueCriteria criteria);

    R visit(@Nonnull StringContainsRepeatedSpacesCriteria criteria);

    R visit(@Nonnull StringContainsRegexMatchCriteria criteria);

}
