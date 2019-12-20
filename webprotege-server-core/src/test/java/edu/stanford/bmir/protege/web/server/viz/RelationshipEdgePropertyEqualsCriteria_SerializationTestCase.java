package edu.stanford.bmir.protege.web.server.viz;

import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.viz.AnySubClassOfEdgeCriteria;
import edu.stanford.bmir.protege.web.shared.viz.EdgeCriteria;
import edu.stanford.bmir.protege.web.shared.viz.RelationshipEdgePropertyEqualsCriteria;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLProperty;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
public class RelationshipEdgePropertyEqualsCriteria_SerializationTestCase {

    private OWLProperty property = MockingUtils.mockOWLObjectProperty();

    @Test
    public void shouldSerialize_RelationshipEdgePropertyEqualsCriteria() throws IOException {
        testSerialization(RelationshipEdgePropertyEqualsCriteria.get(property));
    }

    private static <V extends EdgeCriteria> void testSerialization(V value) throws IOException {
        JsonSerializationTestUtil.testSerialization(value, EdgeCriteria.class);
    }
}
