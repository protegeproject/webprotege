package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.shared.project.BranchId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.owl2lpg.client.read.ontology.ProjectAccessor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class Neo4jProjectBranchManager implements ProjectBranchManager {

  @Nonnull
  private final ProjectId projectId;

  @Nonnull
  private final ProjectAccessor projectAccessor;

  @Inject
  public Neo4jProjectBranchManager(@Nonnull ProjectId projectId,
                                   @Nonnull ProjectAccessor projectAccessor) {
    this.projectId = checkNotNull(projectId);
    this.projectAccessor = checkNotNull(projectAccessor);
  }

  @Nonnull
  @Override
  public BranchId getDefaultBranchId() {
    return projectAccessor.getDefaultBranchId(projectId)
        .orElse(BranchId.get(UUID.randomUUID().toString(), true));
  }
}
