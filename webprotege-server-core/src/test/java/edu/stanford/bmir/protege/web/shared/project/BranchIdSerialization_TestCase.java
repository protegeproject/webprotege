package edu.stanford.bmir.protege.web.shared.project;

import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-20
 */
public class BranchIdSerialization_TestCase {

    @Test
    public void shouldSerializeBranchId() throws IOException {
        JsonSerializationTestUtil.testSerialization(BranchId.generate(),
                                                    BranchId.class);
    }
}
