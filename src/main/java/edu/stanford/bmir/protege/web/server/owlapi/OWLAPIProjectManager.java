package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectAlreadyExistsException;
import edu.stanford.bmir.protege.web.shared.project.ProjectDocumentNotFoundException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/03/2012
 */
public class OWLAPIProjectManager {

    private static OWLAPIProjectManager instance = new OWLAPIProjectManager();

    private final OWLAPIProjectCache projectCache;

    private OWLAPIProjectManager() {
        projectCache = new OWLAPIProjectCache();
    }

    public static synchronized  OWLAPIProjectManager getProjectManager() {
        return instance;
    }
    
    public OWLAPIProject getProject(ProjectId projectId) throws ProjectDocumentNotFoundException {
        return projectCache.getProject(projectId);
    }
    
    public OWLAPIProject createNewProject(NewProjectSettings newProjectSettings) throws ProjectAlreadyExistsException {
        return projectCache.getProject(newProjectSettings);
    }

    public long getLastAccessTime(ProjectId projectId) {
        return projectCache.getLastAccessTime(projectId);
    }
}
