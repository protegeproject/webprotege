package edu.stanford.bmir.protege.web.server.viz;

import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;
import edu.stanford.bmir.protege.web.shared.viz.AnyEdgeCriteria;
import edu.stanford.bmir.protege.web.shared.viz.EdgeCriteria;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
public class AnyEdgeCriteria_SerializationTestCase {

    @Test
    public void shouldSerialize_AnyEdgeCriteria() throws IOException {
        testSerialization(AnyEdgeCriteria.get());
    }

    private static <V extends EdgeCriteria> void testSerialization(V value) throws IOException {
        JsonSerializationTestUtil.testSerialization(value, EdgeCriteria.class);
    }
}
