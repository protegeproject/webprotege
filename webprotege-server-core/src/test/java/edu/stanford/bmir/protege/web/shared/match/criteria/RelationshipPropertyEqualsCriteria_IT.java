package edu.stanford.bmir.protege.web.shared.match.criteria;

import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-04
 */
public class RelationshipPropertyEqualsCriteria_IT {

    private OWLObjectProperty objectProperty;

    @Before
    public void setUp() {
        objectProperty = MockingUtils.mockOWLObjectProperty();
    }

    @Test
    public void shouldSerialize_AnyRelationshipPropertyCriteria() throws IOException {
        testSerialization(RelationshipPropertyEqualsCriteria.get(
            objectProperty
        ));
    }

    private static <V extends RelationshipPropertyCriteria> void testSerialization(V value) throws IOException {
        JsonSerializationTestUtil.testSerialization(value, RelationshipPropertyCriteria.class);
    }
}
