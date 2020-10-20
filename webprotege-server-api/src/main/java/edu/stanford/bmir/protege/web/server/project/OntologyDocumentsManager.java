package edu.stanford.bmir.protege.web.server.project;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.project.BranchId;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-20
 */
public interface OntologyDocumentsManager {

    @Nonnull
    ImmutableSet<OntologyDocumentId> getOntologyDocuments(@Nonnull ProjectId projectId,
                                                          @Nonnull BranchId branchId);

    @Nonnull
    OntologyDocumentId getDefaultOntologyDocument(@Nonnull ProjectId projectId,
                                                  @Nonnull BranchId branchId);
}
