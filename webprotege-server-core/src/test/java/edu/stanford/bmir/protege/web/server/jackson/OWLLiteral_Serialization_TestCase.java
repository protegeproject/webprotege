package edu.stanford.bmir.protege.web.server.jackson;

import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.io.IOException;

public class OWLLiteral_Serialization_TestCase {

    private OWLDataFactory dataFactory;

    @Before
    public void setUp() throws Exception {
        dataFactory = new OWLDataFactoryImpl();
    }

    @Test
    public void shouldRoundTrip_OWLLiteral_with_LangTag() throws IOException {
        var literal = dataFactory.getOWLLiteral("Hello", "en");
        JsonSerializationTestUtil.testSerialization(literal, OWLLiteral.class);
    }

    @Test
    public void shouldRoundTrip_OWLLiteral_with_Datatype() throws IOException {
        var literal = dataFactory.getOWLLiteral(33);
        JsonSerializationTestUtil.testSerialization(literal, OWLLiteral.class);
    }


    @Test
    public void shouldRoundTrip_OWLLiteral_with_Datatype_Against_OWLAnnotationValue() throws IOException {
        var literal = dataFactory.getOWLLiteral(33);
        JsonSerializationTestUtil.testSerialization(literal, OWLLiteral.class);
    }

    @Test
    public void shouldRoundTrip_OWLLiteral_wit_Empty_LangTag() throws IOException {
        var literal = dataFactory.getOWLLiteral("Hello", "");
        JsonSerializationTestUtil.testSerialization(literal, OWLLiteral.class);
    }

    @Test
    public void shouldRoundTrip_OWLLiteral_with_LangTag_Against_OWLAnnotationValue() throws IOException {
        var literal = dataFactory.getOWLLiteral("Hello", "en");
        JsonSerializationTestUtil.testSerialization(literal, OWLAnnotationValue.class);
    }

    @Test
    public void shouldRoundTrip_OWLLiteral_wit_Empty_LangTag_Against_OWLAnnotationValue() throws IOException {
        var literal = dataFactory.getOWLLiteral("Hello", "");
        JsonSerializationTestUtil.testSerialization(literal, OWLAnnotationValue.class);
    }
}
