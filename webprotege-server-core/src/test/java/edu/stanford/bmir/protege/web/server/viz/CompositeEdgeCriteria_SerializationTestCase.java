package edu.stanford.bmir.protege.web.server.viz;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
import edu.stanford.bmir.protege.web.shared.viz.*;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-07
 */
public class CompositeEdgeCriteria_SerializationTestCase {

    @Test
    public void shouldSerialize_AnyEdgeCriteria() throws IOException {
        testSerialization(CompositeEdgeCriteria.get(
                ImmutableList.of(
                        AnySubClassOfEdgeCriteria.get(),
                        AnyInstanceOfEdgeCriteria.get()
                ),
                MultiMatchType.ANY
        ));
    }

    private static <V extends EdgeCriteria> void testSerialization(V value) throws IOException {
        JsonSerializationTestUtil.testSerialization(value, EdgeCriteria.class);
    }

}
