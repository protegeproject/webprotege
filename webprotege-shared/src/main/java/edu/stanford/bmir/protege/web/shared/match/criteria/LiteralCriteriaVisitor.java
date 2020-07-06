package edu.stanford.bmir.protege.web.shared.match.criteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
public interface LiteralCriteriaVisitor<R> {

    default R doDefault(LiteralCriteria criteria) {
        return null;
    }

    default R visit(StringEndsWithCriteria stringEndsWithCriteria) { return doDefault(stringEndsWithCriteria); }

    default R visit(StringContainsCriteria stringContainsCriteria) { return doDefault(stringContainsCriteria); };

    default R visit(StringEqualsCriteria stringEqualsCriteria) { return doDefault(stringEqualsCriteria); };

    default R visit(StringStartsWithCriteria stringStartsWithCriteria) { return doDefault(stringStartsWithCriteria); };

    default R visit(StringContainsRegexMatchCriteria stringContainsRegexMatchCriteria) { return doDefault(stringContainsRegexMatchCriteria); };

    default R visit(StringDoesNotContainRegexMatchCriteria stringDoesNotContainRegexMatchCriteria) { return doDefault(stringDoesNotContainRegexMatchCriteria); };

    default R visit(NumericValueCriteria numericValueCriteria) { return doDefault(numericValueCriteria); };

    default R visit(DateIsBeforeCriteria dateIsBeforeCriteria) { return doDefault(dateIsBeforeCriteria); };

    default R visit(DateIsAfterCriteria dateIsAfterCriteria) { return doDefault(dateIsAfterCriteria); };

    default R visit(StringContainsRepeatedSpacesCriteria stringContainsRepeatedSpacesCriteria) { return doDefault(stringContainsRepeatedSpacesCriteria); };

    default R visit(StringHasUntrimmedSpaceCriteria stringHasUntrimmedSpaceCriteria) { return doDefault(stringHasUntrimmedSpaceCriteria); };

    default R visit(AnyLangTagOrEmptyLangTagCriteria anyLangTagOrEmptyLangTagCriteria) { return doDefault(anyLangTagOrEmptyLangTagCriteria); };

    default R visit(LangTagIsEmptyCriteria langTagIsEmptyCriteria) { return doDefault(langTagIsEmptyCriteria); };

    default R visit(LangTagMatchesCriteria langTagMatchesCriteria) { return doDefault(langTagMatchesCriteria); };

    default R visit(CompositeLiteralCriteria compositeLiteralCriteria) { return doDefault(compositeLiteralCriteria); };
}
