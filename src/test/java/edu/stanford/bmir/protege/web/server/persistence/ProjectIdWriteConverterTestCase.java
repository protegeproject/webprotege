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
public class ProjectIdWriteConverterTestCase {

    @Test
    public void convertShouldReturnSuppliedId() {
        ProjectIdWriteConverter converter = new ProjectIdWriteConverter();
        String suppliedId = UUID.randomUUID().toString();
        ProjectId projectId = ProjectId.get(suppliedId);
        String convertedId = converter.convert(projectId);
        assertEquals(suppliedId, convertedId);
    }


}
