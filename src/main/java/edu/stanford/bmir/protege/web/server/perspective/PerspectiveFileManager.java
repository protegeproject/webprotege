package edu.stanford.bmir.protege.web.server.perspective;

import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.server.inject.project.ProjectDirectoryFactory;
import edu.stanford.bmir.protege.web.shared.auth.Md5MessageDigestAlgorithm;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Provider;
import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/16
 */
public class PerspectiveFileManager {

    public static final String EXTENSION = ".json";

    public static final String PERSPECTIVE_DATA_DIRECTORY_NAME = "perspective-data";

    private final File defaultPerspectivesDirectory;

    private final Provider<Md5MessageDigestAlgorithm> algorithmProvider;

    private final ProjectDirectoryFactory projectDirectoryFactory;

    @Inject
    public PerspectiveFileManager(@DefaultPerspectiveDataDirectory File defaultPerspectivesDirectory,
                                  ProjectDirectoryFactory projectDirectoryFactory,
                                  Provider<Md5MessageDigestAlgorithm> algorithmProvider) {
        this.defaultPerspectivesDirectory = defaultPerspectivesDirectory;
        this.projectDirectoryFactory = projectDirectoryFactory;
        this.algorithmProvider = algorithmProvider;
    }

    public File getDefaultPerspectiveFile(PerspectiveId perspectiveId) {
        // Location: default-perspectives/{{PerspectiveIdDigest}}.json
        return new File(defaultPerspectivesDirectory, getPerspectiveIdFileName(perspectiveId));
    }

    public File getDefaultPerspectiveFileForProject(ProjectId projectId, PerspectiveId perspectiveId) {
        //  Location:  {{ProjectId}}/perspective-data/default/{{PerspectiveIdDigest}}.json
        File defaultDirectory = new File(getPerspectiveDataDirectory(projectId), "default");
        return new File(defaultDirectory, getPerspectiveIdFileName(perspectiveId));
    }

    public File getPerspectiveFileForUser(ProjectId projectId, PerspectiveId perspectiveId, UserId userId) {
        //  Location:  {{ProjectId}}/perspective-data/{{UserIdDigest}}/{{PerspectiveIdDigest}}.json
        String userIdDigest = getUserIdDigest(userId);
        File userDirectory = new File(getPerspectiveDataDirectory(projectId), userIdDigest);
        return new File(userDirectory, getPerspectiveIdFileName(perspectiveId));
    }

    private File getPerspectiveDataDirectory(ProjectId projectId) {
        return new File(projectDirectoryFactory.getProjectDirectory(projectId), PERSPECTIVE_DATA_DIRECTORY_NAME);
    }

    private String getPerspectiveIdFileName(PerspectiveId perspectiveId) {
        return getPerspectiveIdDigest(perspectiveId) + EXTENSION;
    }

    private String getPerspectiveIdDigest(PerspectiveId perspectiveId) {
        String id = perspectiveId.getId();
        return getDigest(id);
    }

    private String getUserIdDigest(UserId userId) {
        String userName = userId.getUserName();
        return getDigest(userName);
    }

    private String getDigest(String value) {
        Md5MessageDigestAlgorithm algorithm = algorithmProvider.get();
        algorithm.updateWithBytesFromUtf8String(value);
        return algorithm.computeDigestAsBase16Encoding();
    }
}
