package edu.stanford.bmir.protege.web.server.csv;

import edu.stanford.bmir.protege.web.server.inject.ImportDirectory;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class CsvDirectoryResolverImpl implements CsvDirectoryResolver {

  @Nonnull
  private final File importDirectory;

  @Inject
  public CsvDirectoryResolverImpl(@ImportDirectory @Nonnull File importDirectory) {
    this.importDirectory = checkNotNull(importDirectory);
  }

  @Nonnull
  @Override
  public Path resolve(@Nonnull String directoryName) {
    return importDirectory.toPath().resolve(directoryName);
  }
}
