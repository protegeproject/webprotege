package edu.stanford.bmir.protege.web.server.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.*;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.regex.Pattern;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-19
 */
public class LiteralMatcherFactory {

    @Inject
    public LiteralMatcherFactory() {
    }

    public Matcher<OWLLiteral> getMatcher(@Nonnull LiteralCriteria literalCriteria) {
        return literalCriteria.accept(new Mapper());
    }


    private static class Mapper implements LiteralCriteriaVisitor<Matcher<OWLLiteral>> {
        @Override
        public Matcher<OWLLiteral> visit(StringEndsWithCriteria criteria) {
            return LiteralMatcher.forLexicalValue(new StringEndsWithMatcher(criteria));
        }

        @Override
        public Matcher<OWLLiteral> visit(StringContainsCriteria criteria) {
            return LiteralMatcher.forLexicalValue(new StringContainsMatcher(criteria));
        }

        @Override
        public Matcher<OWLLiteral> visit(StringEqualsCriteria criteria) {
            return LiteralMatcher.forLexicalValue(new StringEqualsMatcher(criteria));
        }

        @Override
        public Matcher<OWLLiteral> visit(StringStartsWithCriteria criteria) {
            return LiteralMatcher.forLexicalValue(new StringStartsWithMatcher(criteria));
        }

        @Override
        public Matcher<OWLLiteral> visit(StringContainsRegexMatchCriteria criteria) {
            return LiteralMatcher.forLexicalValue(new StringContainsRegexMatchMatcher(Pattern.compile(criteria.getPattern())));
        }

        @Override
        public Matcher<OWLLiteral> visit(StringDoesNotContainRegexMatchCriteria criteria) {
            return LiteralMatcher.forLexicalValue(new NotMatcher<>(new StringContainsRegexMatchMatcher(Pattern.compile(criteria.getPattern()))));
        }

        @Override
        public Matcher<OWLLiteral> visit(NumericValueCriteria criteria) {
            return LiteralMatcher.forLexicalValue(new NumericValueMatcher(criteria.getPredicate(), criteria.getValue()));
        }

        @Override
        public Matcher<OWLLiteral> visit(DateIsBeforeCriteria criteria) {
            var date = LocalDate.of(criteria.getYear(), criteria.getMonth(), criteria.getDay());
            return LiteralMatcher.forLexicalValue(new DateIsBeforeMatcher(date));
        }

        @Override
        public Matcher<OWLLiteral> visit(DateIsAfterCriteria criteria) {
            var date = LocalDate.of(criteria.getYear(), criteria.getMonth(), criteria.getDay());
            return LiteralMatcher.forLexicalValue(new DateIsAfterMatcher(date));
        }

        @Override
        public Matcher<OWLLiteral> visit(StringContainsRepeatedSpacesCriteria stringContainsRepeatedSpacesCriteria) {
            return LiteralMatcher.forLexicalValue(new StringContainsRepeatedWhiteSpaceMatcher());
        }

        @Override
        public Matcher<OWLLiteral> visit(StringHasUntrimmedSpaceCriteria stringHasUntrimmedSpaceCriteria) {
            return LiteralMatcher.forLexicalValue(new StringHasUntrimmedSpaceMatcher());
        }

        @Override
        public Matcher<OWLLiteral> visit(AnyLangTagOrEmptyLangTagCriteria criteria) {
            return Matcher.matchesAny();
        }

        @Override
        public Matcher<OWLLiteral> visit(LangTagIsEmptyCriteria criteria) {
            return new LangTagIsEmptyMatcher();
        }

        @Override
        public Matcher<OWLLiteral> visit(LangTagMatchesCriteria criteria) {
            return LiteralMatcher.forLangTag(LangTagMatchesMatcher.fromPattern(criteria.getLanguageRange()));
        }

        @Override
        public Matcher<OWLLiteral> visit(CompositeLiteralCriteria compositeCriteria) {
            var matchers = compositeCriteria.getCriteria()
                             .stream()
                             .map(criteria -> criteria.accept(this))
                             .collect(toImmutableList());
            if(compositeCriteria.getMultiMatchType() == MultiMatchType.ANY) {
                return new OrMatcher<>(matchers);
            }
            else {
                return new AndMatcher<>(matchers);
            }
        }
    }
}
