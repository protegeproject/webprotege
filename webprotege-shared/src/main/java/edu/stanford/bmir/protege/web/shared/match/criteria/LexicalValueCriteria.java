package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 *
 * The criteria for matching a literal lexical value
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "match"
)
@JsonSubTypes({
        @Type(AnyStringCriteria.class),
        @Type(StringStartsWithCriteria.class),
        @Type(StringEndsWithCriteria.class),
        @Type(StringContainsCriteria.class),
        @Type(StringEqualsCriteria.class),
        @Type(StringContainsRegexMatchCriteria.class),
        @Type(StringDoesNotContainRegexMatchCriteria.class),
        @Type(NumericValueCriteria.class),
        @Type(DateIsBeforeCriteria.class),
        @Type(DateIsAfterCriteria.class)
})
public interface LexicalValueCriteria extends Criteria {

    <R> R accept(@Nonnull LexicalValueCriteriaVisitor<R> visitor);
}
