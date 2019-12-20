package edu.stanford.bmir.protege.web.shared.match.criteria;

import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-04
 */
public class AnyRelationshipPropertyCriteria_IT {


        @Test
        public void shouldSerialize_AnyRelationshipPropertyCriteria() throws IOException {
            testSerialization(AnyRelationshipPropertyCriteria.get());
        }

    private static <V extends RelationshipPropertyCriteria> void testSerialization(V value) throws IOException {
        JsonSerializationTestUtil.testSerialization(value, RelationshipPropertyCriteria.class);
    }

}
