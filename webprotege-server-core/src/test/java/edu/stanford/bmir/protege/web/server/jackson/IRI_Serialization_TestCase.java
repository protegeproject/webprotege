package edu.stanford.bmir.protege.web.server.jackson;

import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationValue;

import java.io.IOException;

import static edu.stanford.bmir.protege.web.MockingUtils.mockIRI;

public class IRI_Serialization_TestCase {

    @Test
    public void shouldRoundTrip_IRI() throws IOException {
        var iri = mockIRI();
        JsonSerializationTestUtil.testSerialization(iri, IRI.class);
    }

    @Test(timeout = 1000)
    public void shouldRoundTrip_IRI_Against_OWLAnntationValue() throws IOException {
        var iri = mockIRI();
        JsonSerializationTestUtil.testSerialization(iri, OWLAnnotationValue.class);
    }
}
