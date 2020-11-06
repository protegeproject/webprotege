package edu.stanford.bmir.protege.web.server.csv;

import javax.annotation.Nonnull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public interface CsvImporter {

  boolean loadOntologyFromDirectory(@Nonnull String directoryName);
}
