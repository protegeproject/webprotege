package edu.stanford.bmir.protege.web.server.owlapi;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectAlreadyExistsException;
import edu.stanford.bmir.protege.web.shared.project.ProjectDocumentNotFoundException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/03/2012
 */
public class OWLAPIProjectManager {

//    private static OWLAPIProjectManager instance = new OWLAPIProjectManager();

    private final OWLAPIProjectCache projectCache;

    @Inject
    public OWLAPIProjectManager(OWLAPIProjectCache projectCache) {
        this.projectCache = projectCache;
    }


    
    public OWLAPIProject getProject(ProjectId projectId) throws ProjectDocumentNotFoundException {
        return projectCache.getProject(projectId);
    }

    public Optional<OWLAPIProject> getProjectIfActive(ProjectId projectId) throws ProjectDocumentNotFoundException {
        return projectCache.getProjectIfActive(projectId);
    }

    public boolean isActive(ProjectId projectId) {
        return projectCache.isActive(projectId);
    }
    
    public OWLAPIProject createNewProject(NewProjectSettings newProjectSettings) throws ProjectAlreadyExistsException, OWLOntologyCreationException, IOException, OWLOntologyStorageException {
        return projectCache.getProject(newProjectSettings);
    }

    public long getLastAccessTime(ProjectId projectId) {
        return projectCache.getLastAccessTime(projectId);
    }
}
