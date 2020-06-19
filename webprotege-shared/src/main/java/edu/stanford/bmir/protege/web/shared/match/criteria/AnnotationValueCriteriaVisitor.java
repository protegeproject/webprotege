package edu.stanford.bmir.protege.web.shared.match.criteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
public interface AnnotationValueCriteriaVisitor<R> extends LangTagCriteriaVisitor<R> {

    R visit(@Nonnull AnyAnnotationValueCriteria criteria);

    R visit(@Nonnull IriEqualsCriteria criteria);

    R visit(@Nonnull IriHasAnnotationCriteria criteria);

    R visit(@Nonnull CompositeAnnotationValueCriteria criteria);

    R visit(@Nonnull StringStartsWithCriteria criteria);

    R visit(@Nonnull StringEndsWithCriteria criteria);

    R visit(@Nonnull StringContainsCriteria criteria);

    R visit(@Nonnull StringEqualsCriteria criteria);

    R visit(@Nonnull NumericValueCriteria criteria);

    R visit(@Nonnull StringContainsRepeatedSpacesCriteria criteria);

    R visit(@Nonnull StringHasUntrimmedSpaceCriteria criteria);

    R visit(@Nonnull StringContainsRegexMatchCriteria criteria);

    R visit(@Nonnull StringDoesNotContainRegexMatchCriteria criteria);

    R visit(@Nonnull DateIsBeforeCriteria criteria);

    R visit(@Nonnull DateIsAfterCriteria criteria);

    R visit(@Nonnull LiteralLexicalValueNotInDatatypeLexicalSpaceCriteria criteria);

    @Override
    R visit(@Nonnull LangTagMatchesCriteria criteria);

    @Override
    R visit(@Nonnull LangTagIsEmptyCriteria criteria);

    @Override
    R visit(@Nonnull AnyLangTagOrEmptyLangTagCriteria criteria);

    R visit(CompositeLiteralCriteria compositeLiteralCriteria);
}
