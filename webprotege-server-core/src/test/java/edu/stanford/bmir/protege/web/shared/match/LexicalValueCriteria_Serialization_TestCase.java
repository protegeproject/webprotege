package edu.stanford.bmir.protege.web.shared.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.*;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jun 2018
 */
public class LexicalValueCriteria_Serialization_TestCase {

    @Test
    public void shouldSerialize_StringStartsWithCriteria_IgnoreCaseTrue() throws IOException {
        StringStartsWithCriteria value = StringStartsWithCriteria.get("A", true);
        testSerialization(value);
    }

    @Test
    public void shouldSerialize_StringStartsWithCriteria_IgnoreCaseFalse() throws IOException {
        StringStartsWithCriteria value = StringStartsWithCriteria.get("A", false);
        testSerialization(value);
    }

    @Test
    public void shouldSerialize_StringEndsWithCriteria_IgnoreCaseTrue() throws IOException {
        StringEndsWithCriteria value = StringEndsWithCriteria.get("A", true);
        testSerialization(value);
    }

    @Test
    public void shouldSerialize_StringEndsWithCriteria_IgnoreCaseFalse() throws IOException {
        StringEndsWithCriteria value = StringEndsWithCriteria.get("A", false);
        testSerialization(value);
    }

    @Test
    public void shouldSerialize_StringContainsCriteria_IgnoreCaseTrue() throws IOException {
        StringContainsCriteria value = StringContainsCriteria.get("A", true);
        testSerialization(value);
    }

    @Test
    public void shouldSerialize_StringContainsCriteria_IgnoreCaseFalse() throws IOException {
        StringContainsCriteria value = StringContainsCriteria.get("A", false);
        testSerialization(value);
    }

    @Test
    public void shouldSerialize_StringEqualsCriteria_IgnoreCaseTrue() throws IOException {
        StringEqualsCriteria value = StringEqualsCriteria.get("A", true);
        testSerialization(value);
    }

    @Test
    public void shouldSerialize_StringEqualsCriteria_IgnoreCaseFalse() throws IOException {
        StringEqualsCriteria value = StringEqualsCriteria.get("A", false);
        testSerialization(value);
    }

    @Test
    public void shouldSerialize_StringContainsRegexMatchCriteria_IgnoreCaseFalse() throws IOException {
        testSerialization(
                StringContainsRegexMatchCriteria.get("[A-Z]", false)
        );
    }


    @Test
    public void shouldSerialize_StringContainsRegexMatchCriteria_IgnoreCaseTrue() throws IOException {
        testSerialization(
                StringContainsRegexMatchCriteria.get("[A-Z]", true)
        );
    }


    @Test
    public void shouldSerialize_StringDoesNotContainRegexMatchCriteria_IgnoreCaseFalse() throws IOException {
        testSerialization(
                StringDoesNotContainRegexMatchCriteria.get("[A-Z]", false)
        );
    }


    @Test
    public void shouldSerialize_StringDoesNotContainRegexMatchCriteria_IgnoreCaseTrue() throws IOException {
        testSerialization(
                StringDoesNotContainRegexMatchCriteria.get("[A-Z]", true)
        );
    }

    @Test
    public void shouldSerialize_NumericValueCriteria_LessThan() throws IOException {
        testSerialization(
                NumericValueCriteria.get(NumericPredicate.LESS_THAN, 33)
        );
    }

    @Test
    public void shouldSerialize_NumericValueCriteria_LessThanOrEqualTo() throws IOException {
        testSerialization(
                NumericValueCriteria.get(NumericPredicate.LESS_THAN_OR_EQUAL_TO, 33)
        );
    }

    @Test
    public void shouldSerialize_NumericValueCriteria_GreaterThan() throws IOException {
        testSerialization(
                NumericValueCriteria.get(NumericPredicate.GREATER_THAN, 33)
        );
    }

    @Test
    public void shouldSerialize_NumericValueCriteria_GreaterThanOrEqualTo() throws IOException {
        testSerialization(
                NumericValueCriteria.get(NumericPredicate.GREATER_THAN_OR_EQUAL_TO, 33)
        );
    }

    @Test
    public void shouldSerialize_NumericValueCriteria_EqualTo() throws IOException {
        testSerialization(
                NumericValueCriteria.get(NumericPredicate.IS_EQUAL_TO, 33)
        );
    }

    @Test
    public void shouldSerialize_DateIsBeforeCriteria() throws IOException {
        testSerialization(
                DateIsBeforeCriteria.get(2018, 9, 1)
        );
    }

    @Test
    public void shouldSerialize_DateIsAfterCriteria() throws IOException {
        testSerialization(
                DateIsAfterCriteria.get(2018, 9, 1)
        );
    }

    @Test
    public void shouldSerialize_StringHasUntrimmedSpaceCriteria() throws IOException {
        testSerialization(
                StringHasUntrimmedSpaceCriteria.get()
        );
    }

    @Test
    public void shouldSerialize_StringContainsRepeatedSpacesCriteria() throws IOException {
        testSerialization(
                StringContainsRepeatedSpacesCriteria.get()
        );
    }

    private static <V extends LexicalValueCriteria> void testSerialization(V value) throws IOException {
        JsonSerializationTestUtil.testSerialization(value, AnnotationValueCriteria.class);
    }
}
