package edu.stanford.bmir.protege.web.server.graph;

import edu.stanford.bmir.protege.web.shared.project.BranchId;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public interface GraphManager {

  void setDefaultBranchId(@Nonnull ProjectId projectId,
                          @Nonnull BranchId defaultBranchId);

  void setDefaultOntologyDocumentId(@Nonnull ProjectId projectId,
                                    @Nonnull BranchId branchId,
                                    @Nonnull OntologyDocumentId defaultOntDocId);
}
