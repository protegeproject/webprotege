package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Test;

import java.util.UUID;

import static junit.framework.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 */
public class ProjectIdReadConverterTestCase {

    @Test
    public void convertShouldReturnProjectIdWithSuppliedId() {
        ProjectIdReadConverter converter = new ProjectIdReadConverter();
        String suppliedId = UUID.randomUUID().toString();
        ProjectId projectId = converter.convert(suppliedId);
        assertEquals(suppliedId, projectId.getId());
    }
}
