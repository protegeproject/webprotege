package edu.stanford.bmir.protege.web.server.upload;

import edu.stanford.bmir.protege.web.server.inject.ImportDirectory;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class CsvDocumentResolverImpl implements CsvDocumentResolver {

    @Nonnull
    private final File importDirectory;

    @Inject
    public CsvDocumentResolverImpl(@ImportDirectory @Nonnull File importDirectory) {
        this.importDirectory = checkNotNull(importDirectory);
    }

    @Override
    public Path resolve(@Nonnull ProjectId projectId) {
        return importDirectory.toPath().resolve(projectId.getId());
    }
}
