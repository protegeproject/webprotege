package edu.stanford.bmir.protege.web.server.download;

import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Apr 2017
 */
@ApplicationSingleton
public class ProjectDownloadCache {


    private final ProjectDownloadCacheDirectorySupplier resolver;

    @Inject
    public ProjectDownloadCache(@Nonnull ProjectDownloadCacheDirectorySupplier resolver) {
        this.resolver = checkNotNull(resolver);
    }

    /**
     * Gets the path to where the specified project can be downloaded
     *
     * @param projectId      The projectId of the project to be downloaded.
     * @param revisionNumber The revision number of the project to be downloaded
     * @param downloadFormat The format of the download
     * @return A path to the project revision that has the specified format. If the path
     * does not point to a file that exists then there is no cached download for the specified
     * project revision in the specified format.
     */
    @Nonnull
    public Path getCachedDownloadPath(@Nonnull ProjectId projectId,
                                      @Nonnull RevisionNumber revisionNumber,
                                      @Nonnull DownloadFormat downloadFormat) {
        // If the revision is the head revision then we need to find the real number
        String fileName = String.format("%s-R%d.%s.zip",
                                        projectId.getId(),
                                        revisionNumber.getValue(),
                                        downloadFormat.getExtension());
        String directoryName = projectId.getId();
        Path relativePath = Paths.get(directoryName, fileName);
        return resolver.get().resolve(relativePath);
    }

}
