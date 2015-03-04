package edu.stanford.bmir.protege.web.server.metaproject;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/03/15
 */
public class ProjectExistsFilterImpl implements ProjectExistsFilter {

    private MetaProject metaProject;

    private final Set<ProjectId> missingProjects = new HashSet<>();


    @Inject
    public ProjectExistsFilterImpl(MetaProject metaProject) {
        this.metaProject = metaProject;
        findMissingProjects();
    }

    private void findMissingProjects() {
        for(ProjectInstance project : metaProject.getProjects()) {
            if(ProjectId.isWelFormedProjectId(project.getName())) {
                ProjectId projectId = ProjectId.get(project.getName());
                if(!OWLAPIProjectDocumentStore.getProjectDocumentStore(projectId).exists()) {
                    missingProjects.add(projectId);
                }
            }
        }
    }

    @Override
    public boolean isProjectPresent(ProjectId projectId) {
        return !missingProjects.contains(projectId);
    }
}
