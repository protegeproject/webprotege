package edu.stanford.bmir.protege.web.server.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.*;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
public class StringMatcherFactory {

    public Matcher<String> getMatcher(@Nonnull LexicalValueCriteria criteria) {
        return criteria.accept(new LexicalValueCriteriaVisitor<Matcher<String>>() {
            @Override
            public Matcher<String> visit(@Nonnull AnyStringCriteria criteria) {
                return s -> true;
            }

            @Override
            public Matcher<String> visit(@Nonnull StringStartsWithCriteria criteria) {
                if (criteria.isIgnoreCase()) {
                    return s -> StringUtils.startsWithIgnoreCase(s, criteria.getValue());
                }
                else {
                    return s -> s.startsWith(criteria.getValue());
                }
            }

            @Override
            public Matcher<String> visit(@Nonnull StringEndsWithCriteria criteria) {
                if (criteria.isIgnoreCase()) {
                    return s -> StringUtils.endsWithIgnoreCase(s, criteria.getValue());
                }
                else {
                    return s -> s.endsWith(criteria.getValue());
                }
            }

            @Override
            public Matcher<String> visit(@Nonnull StringContainsCriteria criteria) {
                if (criteria.isIgnoreCase()) {
                    return s -> StringUtils.containsIgnoreCase(s, criteria.getValue());
                }
                else {
                    return s -> s.contains(criteria.getValue());
                }
            }

            @Override
            public Matcher<String> visit(@Nonnull StringEqualsCriteria criteria) {
                if (criteria.isIgnoreCase()) {
                    return s -> s.equalsIgnoreCase(criteria.getValue());
                }
                else {
                    return s -> s.equals(criteria.getValue());
                }
            }

            @Override
            public Matcher<String> visit(@Nonnull NumericValueCriteria criteria) {
                return new NumericValueMatcher(criteria.getPredicate(), criteria.getValue());
            }

            @Override
            public Matcher<String> visit(@Nonnull StringContainsRepeatedSpacesCriteria criteria) {
                return new StringContainsRepeatedWhiteSpaceMatcher();
            }

            @Override
            public Matcher<String> visit(@Nonnull StringContainsRegexMatchCriteria criteria) {
                return new StringContainsRegexMatchMatcher(Pattern.compile(criteria.getPattern()));
            }

            @Override
            public Matcher<String> visit(@Nonnull StringHasUntrimmedSpaceCriteria criteria) {
                return new StringHasUntrimmedSpaceMatcher();
            }
        });
    }
}
