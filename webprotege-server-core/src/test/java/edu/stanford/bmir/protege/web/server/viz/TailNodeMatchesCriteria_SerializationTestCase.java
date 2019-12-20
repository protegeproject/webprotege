package edu.stanford.bmir.protege.web.server.viz;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityTypeIsOneOfCriteria;
import edu.stanford.bmir.protege.web.shared.viz.EdgeCriteria;
import edu.stanford.bmir.protege.web.shared.viz.HeadNodeMatchesCriteria;
import edu.stanford.bmir.protege.web.shared.viz.TailNodeMatchesCriteria;
import org.junit.Test;
import org.semanticweb.owlapi.model.EntityType;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
public class TailNodeMatchesCriteria_SerializationTestCase {


    @Test
    public void shouldSerialize_TailNodeMatchesCriteria() throws IOException {
        testSerialization(TailNodeMatchesCriteria.get(EntityTypeIsOneOfCriteria.get(ImmutableSet.of(EntityType.CLASS))));
    }

    private static <V extends EdgeCriteria> void testSerialization(V value) throws IOException {
        JsonSerializationTestUtil.testSerialization(value, EdgeCriteria.class);
    }
}
