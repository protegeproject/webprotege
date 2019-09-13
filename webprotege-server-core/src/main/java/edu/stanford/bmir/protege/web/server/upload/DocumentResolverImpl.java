package edu.stanford.bmir.protege.web.server.upload;

import edu.stanford.bmir.protege.web.server.inject.UploadsDirectory;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-29
 */
public class DocumentResolverImpl implements DocumentResolver {


    @Nonnull
    private final File uploadsDirectory;

    @Inject
    public DocumentResolverImpl(@UploadsDirectory @Nonnull File uploadsDirectory) {
        this.uploadsDirectory = checkNotNull(uploadsDirectory);
    }

    @Override
    public Path resolve(@Nonnull DocumentId documentId) {
        return uploadsDirectory.toPath().resolve(documentId.getDocumentId());
    }
}
