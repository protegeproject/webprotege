package edu.stanford.bmir.protege.web.server.inject;

import edu.stanford.bmir.protege.web.server.inject.project.ProjectDirectoryFactory;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-29
 */
public class ChangeHistoryFileFactory {

    private static final String CHANGE_DATA_DIRECTORY_NAME = "change-data";

    private static final String CHANGE_DATA_FILE_NAME = "change-data.binary";

    @Nonnull
    private final ProjectDirectoryFactory projectDirectoryFactory;

    @Inject
    public ChangeHistoryFileFactory(@Nonnull ProjectDirectoryFactory projectDirectoryFactory) {
        this.projectDirectoryFactory = checkNotNull(projectDirectoryFactory);
    }

    public File getChangeHistoryFile(@Nonnull ProjectId projectId) {
        checkNotNull(projectId);
        var projectDirectory = projectDirectoryFactory.getProjectDirectory(projectId);
        return new File(new File(projectDirectory, CHANGE_DATA_DIRECTORY_NAME), CHANGE_DATA_FILE_NAME);
    }
}
