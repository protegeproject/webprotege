package edu.stanford.bmir.protege.web.shared.match.criteria;

import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.match.RelationshipPresence;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-04
 */
public class EntityRelationshipCriteria_IT {

    @Test
    public void shouldSerialize_AnyRelationshipPropertyCriteria() throws IOException {
        testSerialization(EntityRelationshipCriteria.get(
                RelationshipPresence.AT_LEAST_ONE,
                AnyRelationshipPropertyCriteria.get(),
                AnyRelationshipValueCriteria.get()
        ));
    }

    private static <V extends EntityRelationshipCriteria> void testSerialization(V value) throws IOException {
        JsonSerializationTestUtil.testSerialization(value, EntityRelationshipCriteria.class);
    }

}
