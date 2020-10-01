package edu.stanford.bmir.protege.web.server.perspective;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
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
public class PerspectiveDescriptorsRecord_Serialization_TestCase {

    @Test
    public void shouldSerialize() throws IOException {
        var record = PerspectiveDescriptorsRecord.get(ProjectId.getNil(),
                                                      UserId.getUserId("Matthew"), ImmutableList.of());
        JsonSerializationTestUtil.testSerialization(record, PerspectiveDescriptorsRecord.class);
    }
}
