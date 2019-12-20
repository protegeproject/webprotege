package edu.stanford.bmir.protege.web.shared.match.criteria;

import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;
import uk.ac.manchester.cs.owl.owlapi.OWLDatatypeImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImplString;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-04
 */
public class RelationshipValueEqualsLiteralCriteria_IT {

    @Test
    public void shouldSerialize_RelationshipValueEqualsLiteralCriteria_String() throws IOException {
        testSerialization(RelationshipValueEqualsLiteralCriteria.get(
                new OWLLiteralImplString("Hello")
        ));
    }

    @Test
    public void shouldSerialize_RelationshipValueEqualsLiteralCriteria_LangString() throws IOException {
        testSerialization(RelationshipValueEqualsLiteralCriteria.get(
                new OWLLiteralImpl("Hello", "en", null)
        ));
    }

    @Test
    public void shouldSerialize_RelationshipValueEqualsLiteralCriteria_TypedString() throws IOException {
        testSerialization(RelationshipValueEqualsLiteralCriteria.get(
                new OWLLiteralImpl("Hello", null, new OWLDatatypeImpl(IRI.create("http://example.org/datatype")))
        ));
    }

    private static <V extends RelationshipValueCriteria> void testSerialization(V value) throws IOException {
        JsonSerializationTestUtil.testSerialization(value, RelationshipValueCriteria.class);
    }
}
