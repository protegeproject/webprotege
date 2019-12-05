package edu.stanford.bmir.protege.web.shared.match.criteria;

import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLClass;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-04
 */
public class RelationshipValueEqualsEntityCriteria_IT {

    private OWLClass cls;

    @Before
    public void setUp() {
        cls = MockingUtils.mockOWLClass();
    }

    @Test
    public void shouldSerialize_AnyRelationshipPropertyCriteria_Entity() throws IOException {
        testSerialization(RelationshipValueEqualsEntityCriteria.get(cls));
    }

    private static <V extends RelationshipValueCriteria> void testSerialization(V value) throws IOException {
        JsonSerializationTestUtil.testSerialization(value, RelationshipValueCriteria.class);
    }
}
