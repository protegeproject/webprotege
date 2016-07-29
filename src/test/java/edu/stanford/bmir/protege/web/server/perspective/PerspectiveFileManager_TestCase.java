package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.server.inject.project.ProjectDirectoryFactory;
import edu.stanford.bmir.protege.web.shared.auth.Md5MessageDigestAlgorithm;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.inject.Provider;
import java.io.File;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/16
 */
@RunWith(MockitoJUnitRunner.class)
public class PerspectiveFileManager_TestCase {

    public static final String DIGEST = "DIGEST";

    public static final String DEFAULT_DIRECTORY = "DEFAULT_DIRECTORY";

    private final File PROJECT_DIRECTORY = new File("PROJECT_DIRECTORY");

    private PerspectiveFileManager fileManager;

    @Mock
    private Md5MessageDigestAlgorithm algorithm;

    @Mock
    private Provider<Md5MessageDigestAlgorithm> algorithmProvider;

    @Mock
    private PerspectiveId perspectiveId;

    @Mock
    private UserId userId;

    private File defaultPerspectivesDirectory = new File(DEFAULT_DIRECTORY);

    @Mock
    private ProjectDirectoryFactory projectDirectoryFactory;

    @Mock
    private ProjectId projectId;

    @Mock
    private DefaultPerspectiveDataCopier defaultPerspectiveDataCopier;

    @Before
    public void setUp() {
        when(algorithm.computeDigestAsBase16Encoding()).thenReturn(DIGEST);
        when(algorithmProvider.get()).thenReturn(algorithm);
        when(projectDirectoryFactory.getProjectDirectory(projectId)).thenReturn(PROJECT_DIRECTORY);
        fileManager = new PerspectiveFileManager(defaultPerspectivesDirectory, projectDirectoryFactory, algorithmProvider, defaultPerspectiveDataCopier);
    }

    @Test
    public void shouldReturnDefaultFile() {
        File defaultDirectory = fileManager.getDefaultPerspectiveLayout(perspectiveId);
        assertThat(defaultDirectory, is(new File("DEFAULT_DIRECTORY/DIGEST.json")));
    }

    @Test
    public void shouldReturnDefaultProjectFile() {
        File defaultDirectory = fileManager.getDefaultPerspectiveLayoutForProject(projectId, perspectiveId);
        assertThat(defaultDirectory, is(new File("PROJECT_DIRECTORY/perspective-data/default/DIGEST.json")));
    }

    @Test
    public void shouldReturnUserFile() {
        File defaultDirectory = fileManager.getPerspectiveLayoutForUser(projectId, perspectiveId, userId);
        assertThat(defaultDirectory, is(new File("PROJECT_DIRECTORY/perspective-data/DIGEST/DIGEST.json")));
    }


}
