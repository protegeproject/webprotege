package edu.stanford.bmir.protege.web.server.viz;

import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.viz.AnyEdgeCriteria;
import edu.stanford.bmir.protege.web.shared.viz.AnyInstanceOfEdgeCriteria;
import edu.stanford.bmir.protege.web.shared.viz.EdgeCriteria;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
public class AnyInstanceOfEdgeCriteria_SerializationTestCase {

    @Test
    public void shouldSerialize_AnyInstanceOfEdgeCriteria() throws IOException {
        testSerialization(AnyInstanceOfEdgeCriteria.get());
    }

    private static <V extends EdgeCriteria> void testSerialization(V value) throws IOException {
        JsonSerializationTestUtil.testSerialization(value, EdgeCriteria.class);
    }
}
