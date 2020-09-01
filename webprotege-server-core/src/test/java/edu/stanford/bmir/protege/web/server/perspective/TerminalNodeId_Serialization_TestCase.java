package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.protege.widgetmap.shared.node.TerminalNodeId;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-30
 */
public class TerminalNodeId_Serialization_TestCase {

    private TerminalNodeId nodeId;

    @Before
    public void setUp() throws Exception {
        nodeId = TerminalNodeId.get();
    }

    @Test
    public void shouldSerializeTerminalNodeId() throws IOException {
        JsonSerializationTestUtil.testSerialization(nodeId, TerminalNodeId.class);
    }
}
