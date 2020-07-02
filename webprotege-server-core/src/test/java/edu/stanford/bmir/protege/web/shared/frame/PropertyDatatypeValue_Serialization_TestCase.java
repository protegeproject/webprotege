package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.bmir.protege.web.MockingUtils.mockOWLDataProperty;
import static edu.stanford.bmir.protege.web.MockingUtils.mockOWLDatatype;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Literal;

public class PropertyDatatypeValue_Serialization_TestCase {


    private PlainPropertyDatatypeValue propertyValue;

    @Before
    public void setUp() throws Exception {
        propertyValue = PlainPropertyDatatypeValue.get(mockOWLDataProperty(),
                                                       mockOWLDatatype());
    }

    @Test
    public void shouldSerializeAndDeserializePropertyValue() throws IOException {
        JsonSerializationTestUtil.testSerialization(propertyValue, PlainPropertyValue.class);
    }
}
