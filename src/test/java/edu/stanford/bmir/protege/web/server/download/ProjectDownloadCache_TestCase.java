package edu.stanford.bmir.protege.web.server.download;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Apr 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectDownloadCache_TestCase {

    private static final String THE_PROJECT_ID = "TheProjectId";

    private static final long REVISION_NUMBER = 33L;

    private ProjectDownloadCache cache;

    @Rule
    public TemporaryFolder temporaryFolder;

    @Mock
    private ProjectDownloadCacheDirectorySupplier directorySupplier;

    @Mock
    private ProjectId projectId;

    @Mock
    private RevisionNumber revisionNumber;

    private DownloadFormat downloadFormat;


    private Path root;



    @Before
    public void setUp() throws Exception {
        when(projectId.getId()).thenReturn(THE_PROJECT_ID);
        when(revisionNumber.getValue()).thenReturn(REVISION_NUMBER);
        downloadFormat = DownloadFormat.RDF_XML;

        root = temporaryFolder.getRoot().toPath();
        when(directorySupplier.get()).thenReturn(root);
        cache = new ProjectDownloadCache(directorySupplier);
    }

    @Test
    public void shouldResolvePath() {
        Path path = cache.getCachedDownloadPath(projectId, revisionNumber, downloadFormat);
        Path expectedPath = root.resolve(THE_PROJECT_ID).resolve(THE_PROJECT_ID + "-" + REVISION_NUMBER + "." + downloadFormat.getExtension() + ".zip");
        assertThat(path, is(expectedPath));
    }
}
