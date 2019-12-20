package edu.stanford.bmir.protege.web.server.viz;

import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.viz.CompositeEdgeCriteria;
import edu.stanford.bmir.protege.web.shared.viz.EntityGraphFilter;
import edu.stanford.bmir.protege.web.shared.viz.FilterName;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-11
 */
public class EntityGraphFilter_SerializationTestCase {

    @Test
    public void shouldSerialize_EntityGraphFilter() throws IOException {
        testSerialization(EntityGraphFilter.get(FilterName.get("TheName"),
                                                "TheDescription",
                                                CompositeEdgeCriteria.anyEdge(),
                                                CompositeEdgeCriteria.noEdge(),
                                                true));
    }

    private static <V extends EntityGraphFilter> void testSerialization(V value) throws IOException {
        JsonSerializationTestUtil.testSerialization(value, EntityGraphFilter.class);
    }
}
