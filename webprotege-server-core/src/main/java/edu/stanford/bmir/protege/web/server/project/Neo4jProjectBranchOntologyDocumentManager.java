package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.shared.project.BranchId;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.owl2lpg.client.read.ontology.ProjectAccessor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Set;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class Neo4jProjectBranchOntologyDocumentManager implements ProjectBranchOntologyDocumentManager {

  @Nonnull
  private final ProjectId projectId;

  @Nonnull
  private final BranchId branchId;

  @Nonnull
  private final ProjectAccessor projectAccessor;

  @Inject
  public Neo4jProjectBranchOntologyDocumentManager(@Nonnull ProjectId projectId,
                                                   @Nonnull BranchId branchId,
                                                   @Nonnull ProjectAccessor projectAccessor) {
    this.projectId = checkNotNull(projectId);
    this.branchId = checkNotNull(branchId);
    this.projectAccessor = checkNotNull(projectAccessor);
  }

  @Nonnull
  @Override
  public OntologyDocumentId getDefaultOntologyDocumentId() {
    return projectAccessor.getDefaultOntologyDocumentId(projectId, branchId)
        .orElse(OntologyDocumentId.get(UUID.randomUUID().toString(), true));
  }

  @Nonnull
  @Override
  public Set<OntologyDocumentId> getOntologyDocumentIds() {
    return projectAccessor.getOntologyDocumentIds(projectId, branchId);
  }
}
