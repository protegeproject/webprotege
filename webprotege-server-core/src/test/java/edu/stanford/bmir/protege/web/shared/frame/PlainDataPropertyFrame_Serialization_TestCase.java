package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.bmir.protege.web.MockingUtils.*;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Literal;

public class PlainDataPropertyFrame_Serialization_TestCase {

    private PlainDataPropertyFrame plainDataPropertyFrame;

    @Before
    public void setUp() throws Exception {
        var subject = mockOWLDataProperty();
        var propertyValues = ImmutableSet.<PlainPropertyAnnotationValue>of(
                PlainPropertyAnnotationValue.get(mockOWLAnnotationProperty(), Literal("Hello"))
        );
        var domains = ImmutableSet.of(mockOWLClass());
        var ranges = ImmutableSet.of(mockOWLDatatype());
        var functional = true;
        plainDataPropertyFrame = PlainDataPropertyFrame.get(subject, propertyValues, domains, ranges, functional);
    }

    @Test
    public void shouldSerializePlainDataPropertyFrame() throws IOException {
        JsonSerializationTestUtil.testSerialization(plainDataPropertyFrame, PlainEntityFrame.class);
    }
}
