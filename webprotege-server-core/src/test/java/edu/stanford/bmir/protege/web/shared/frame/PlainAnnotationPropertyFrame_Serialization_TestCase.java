package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.bmir.protege.web.MockingUtils.*;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Literal;

public class PlainAnnotationPropertyFrame_Serialization_TestCase {

    private PlainAnnotationPropertyFrame plainAnnotationPropertyFrame;

    @Before
    public void setUp() throws Exception {
        var subject = mockOWLAnnotationProperty();
        var propertyValues = ImmutableSet.of(
                PlainPropertyAnnotationValue.get(mockOWLAnnotationProperty(), Literal("Hello"))
        );
        var domains = ImmutableSet.of(mockIRI());
        var ranges = ImmutableSet.of(mockIRI());
        plainAnnotationPropertyFrame = PlainAnnotationPropertyFrame.get(subject, propertyValues, domains, ranges);
    }

    @Test
    public void shouldSerializePlainAnnotationPropertyFrame() throws IOException {
        JsonSerializationTestUtil.testSerialization(plainAnnotationPropertyFrame, PlainEntityFrame.class);
    }
}
