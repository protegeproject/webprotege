package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public interface ProjectBranchOntologyDocumentManager {

  @Nonnull
  OntologyDocumentId getDefaultOntologyDocumentId();

  @Nonnull
  Set<OntologyDocumentId> getOntologyDocumentIds();
}
