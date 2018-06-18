package edu.stanford.bmir.protege.web.shared.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.*;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jun 2018
 */
public class AnnotationCriteria_Serialization_TestCase {

    @Test
    public void shouldSerialize_AnnotationComponentCriteria() throws IOException {
        testSerialization(
                AnnotationComponentCriteria.get(
                        AnyAnnotationPropertyCriteria.get(),
                        AnyAnnotationValueCriteria.get(),
                        AnyAnnotationSetCriteria.get(),
                        AnnotationPresence.NONE
                )
        );
    }

    private static <V extends AnnotationCriteria> void testSerialization(V value) throws IOException {
        JsonSerializationTestUtil.testSerialization(value, AnnotationCriteria.class);
    }
}
