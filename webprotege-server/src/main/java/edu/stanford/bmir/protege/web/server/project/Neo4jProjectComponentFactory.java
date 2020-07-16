package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.app.ServerComponent;
import edu.stanford.bmir.protege.web.server.inject.ProjectComponent;
import edu.stanford.bmir.protege.web.server.inject.project.Neo4jProjectModule;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class Neo4jProjectComponentFactory implements ProjectComponentFactory {

  @Nonnull
  private final ServerComponent serverComponent;

  @Inject
  public Neo4jProjectComponentFactory(@Nonnull ServerComponent serverComponent) {
    this.serverComponent = serverComponent;
  }

  @Nonnull
  @Override
  public ProjectComponent createProjectComponent(@Nonnull ProjectId projectId) {
    return serverComponent.getProjectComponent(new Neo4jProjectModule(projectId));
  }
}
