package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.bmir.protege.web.MockingUtils.*;

public class PropertyIndividual_Serialization_TestCase {

    private PlainPropertyIndividualValue propertyValue;

    @Before
    public void setUp() throws Exception {
        propertyValue = PlainPropertyIndividualValue.get(mockOWLObjectProperty(),
                                                    mockOWLNamedIndividual());
    }

    @Test
    public void shouldSerializeAndDeserializePropertyValue() throws IOException {
        JsonSerializationTestUtil.testSerialization(propertyValue, PlainPropertyValue.class);
    }
}
