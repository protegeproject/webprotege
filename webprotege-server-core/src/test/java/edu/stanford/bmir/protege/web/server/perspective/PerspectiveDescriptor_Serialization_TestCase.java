package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDescriptor;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-01
 */
public class PerspectiveDescriptor_Serialization_TestCase {

    @Test
    public void shouldSerialize() throws IOException {
        var descriptor = PerspectiveDescriptor.get(
                PerspectiveId.generate(), LanguageMap.of("en", "Hello"), true
        );
        JsonSerializationTestUtil.testSerialization(descriptor, PerspectiveDescriptor.class);
    }
}
