package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
public class ProjectLuceneDirectoryPathSupplier implements Supplier<Path> {

    @Inject
    public ProjectLuceneDirectoryPathSupplier() {
    }

    @Override
    public Path get() {
        return Path.of("/tmp/lucene");
    }
}
