package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.SharingSettingsService;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/02/2012
 */
public class SharingSettingsServiceImpl extends WebProtegeRemoteServiceServlet implements SharingSettingsService {

    public ProjectSharingSettings getProjectSharingSettings(ProjectId projectId) {
        return MetaProjectManager.getManager().getProjectSharingSettings(projectId);
    }

    public void updateSharingSettings(ProjectSharingSettings projectSharingSettings) {
        MetaProjectManager.getManager().setProjectSharingSettings(projectSharingSettings);
    }
}
