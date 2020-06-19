package edu.stanford.bmir.protege.web.shared.match.criteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
public interface LiteralCriteriaVisitor<R> {

    R visit(StringEndsWithCriteria stringEndsWithCriteria);

    R visit(StringContainsCriteria stringContainsCriteria);

    R visit(StringEqualsCriteria stringEqualsCriteria);

    R visit(StringStartsWithCriteria stringStartsWithCriteria);

    R visit(StringContainsRegexMatchCriteria stringContainsRegexMatchCriteria);

    R visit(StringDoesNotContainRegexMatchCriteria stringDoesNotContainRegexMatchCriteria);

    R visit(NumericValueCriteria numericValueCriteria);

    R visit(DateIsBeforeCriteria dateIsBeforeCriteria);

    R visit(DateIsAfterCriteria dateIsAfterCriteria);

    R visit(StringContainsRepeatedSpacesCriteria stringContainsRepeatedSpacesCriteria);

    R visit(StringHasUntrimmedSpaceCriteria stringHasUntrimmedSpaceCriteria);

    R visit(AnyLangTagOrEmptyLangTagCriteria anyLangTagOrEmptyLangTagCriteria);

    R visit(LangTagIsEmptyCriteria langTagIsEmptyCriteria);

    R visit(LangTagMatchesCriteria langTagMatchesCriteria);

    R visit(CompositeLiteralCriteria compositeLiteralCriteria);
}
