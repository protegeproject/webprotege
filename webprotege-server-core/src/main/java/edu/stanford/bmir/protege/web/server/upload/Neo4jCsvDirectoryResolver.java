package edu.stanford.bmir.protege.web.server.upload;

import edu.stanford.bmir.protege.web.shared.csv.DocumentId;

import javax.annotation.Nonnull;
import java.nio.file.Path;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class Neo4jCsvDirectoryResolver implements DocumentResolver {

  @Override
  public Path resolve(@Nonnull DocumentId documentId) {
    return null;
  }
}
