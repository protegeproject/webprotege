package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class ProjectLuceneDirectoryPathSupplier_TestCase {

    private ProjectLuceneDirectoryPathSupplier pathSupplier;

    private Path baseDirectoryPath = Path.of("/tmp", "lucene");

    private ProjectId projectId = ProjectId.get("12345678-1234-1234-1234-123456789abc");

    @Before
    public void setUp() throws Exception {
        pathSupplier = new ProjectLuceneDirectoryPathSupplier(baseDirectoryPath, projectId);
    }

    @Test
    public void shouldGetPathForProject() {
        Path path = pathSupplier.get();
        assertThat(path, is(equalTo(Path.of("/tmp", "lucene", projectId.getId()))));
    }
}