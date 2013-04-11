package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.RevisionManagerService;
import edu.stanford.bmir.protege.web.client.rpc.data.RevisionNumber;
import edu.stanford.bmir.protege.web.client.rpc.data.RevisionSummary;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.server.owlapi.change.OWLAPIChangeManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/10/2012
 */
public class RevisionManagerServiceImpl extends WebProtegeRemoteServiceServlet implements RevisionManagerService {

    private OWLAPIProject getProject(ProjectId projectId) {
        OWLAPIProjectManager pm = OWLAPIProjectManager.getProjectManager();
        return pm.getProject(projectId);
    }

    public RevisionNumber getHeadRevisionNumber(ProjectId projectId) {
        OWLAPIProject project = getProject(projectId);
        return project.getRevisionNumber();
    }


    public List<RevisionSummary> getRevisionSummaries(ProjectId projectId) {
        OWLAPIProject project = getProject(projectId);
        OWLAPIChangeManager changeManager = project.getChangeManager();
        return changeManager.getRevisionSummaries();
    }
}
