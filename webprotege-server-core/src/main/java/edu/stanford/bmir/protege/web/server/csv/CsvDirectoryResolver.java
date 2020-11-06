package edu.stanford.bmir.protege.web.server.csv;

import edu.stanford.bmir.protege.web.shared.csv.DocumentId;

import javax.annotation.Nonnull;
import java.nio.file.Path;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public interface CsvDirectoryResolver {

  @Nonnull
  Path resolve(@Nonnull String directoryName);
}
