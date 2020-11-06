package edu.stanford.bmir.protege.web.server.project;

import dagger.Binds;
import dagger.Module;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.owl2lpg.client.read.ontology.ProjectAccessorModule;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
@Module(includes = ProjectAccessorModule.class)
public abstract class Neo4jProjectBranchOntologyDocumentManagerModule {

  @Binds
  @ProjectSingleton
  public abstract ProjectBranchOntologyDocumentManager
  provideProjectBranchOntologyDocumentManager(Neo4jProjectBranchOntologyDocumentManager impl);
}
