package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDescriptor;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-01
 */
public class PerspectiveDescriptorRecord_Serialization_TestCase {

    @Test
    public void shouldSerialize() throws IOException {
        var record = PerspectiveDescriptorRecord.get(ProjectId.getNil(),
                                                     UserId.getUserId("Matthew"),
                                                     PerspectiveId.generate(),
                                                     LanguageMap.of("en", "Hello"),
                                                     true);
        JsonSerializationTestUtil.testSerialization(record, PerspectiveDescriptorRecord.class);
    }
}
