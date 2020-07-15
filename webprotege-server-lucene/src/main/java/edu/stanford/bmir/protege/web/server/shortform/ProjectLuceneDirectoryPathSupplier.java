package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.nio.file.Path;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
public class ProjectLuceneDirectoryPathSupplier implements Supplier<Path> {

    @Nonnull
    private final ProjectId projectId;

    @Inject
    public ProjectLuceneDirectoryPathSupplier(@Nonnull ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    @Override
    public Path get() {
        return Path.of("/tmp/lucene/" + projectId.getId());
    }
}
