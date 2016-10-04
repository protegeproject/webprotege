package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.inject.DataDirectory;
import edu.stanford.bmir.protege.web.server.inject.UploadsDirectory;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Oct 2016
 */
// TODO: Auto-generate this
public class ProjectImporterFactory {

    @Nonnull
    private final File uploadsDirectory;

    @Nonnull
    private final File dataDirectory;

    @Nonnull
    private final WebProtegeLogger logger;

    @Nonnull
    private final UploadedProjectSourcesExtractor uploadedProjectSourcesExtractor;

    @Inject
    public ProjectImporterFactory(
            @Nonnull @UploadsDirectory File uploadsDirectory,
            @Nonnull @DataDirectory File dataDirectory,
            @Nonnull UploadedProjectSourcesExtractor uploadedProjectSourcesExtractor,
            @Nonnull WebProtegeLogger logger) {
        this.uploadsDirectory = checkNotNull(uploadsDirectory);
        this.dataDirectory = checkNotNull(dataDirectory);
        this.uploadedProjectSourcesExtractor = checkNotNull(uploadedProjectSourcesExtractor);
        this.logger = checkNotNull(logger);
    }

    @Nonnull
    public ProjectImporter getProjectImporter(@Nonnull ProjectId projectId) {
        return new ProjectImporter(projectId, uploadsDirectory, dataDirectory, logger, uploadedProjectSourcesExtractor);
    }
}
