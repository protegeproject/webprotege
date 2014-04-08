package edu.stanford.bmir.protege.web.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.stanford.bmir.protege.web.client.rpc.SharingSettingsService;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/02/2012
 */
public class SharingSettingsServiceImpl extends WebProtegeRemoteServiceServlet implements SharingSettingsService {

    public ProjectSharingSettings getProjectSharingSettings(ProjectId projectId) {
        return SharingSettingsManager.getManager().getProjectSharingSettings(projectId);
    }

    public void updateSharingSettings(ProjectSharingSettings projectSharingSettings) {
        SharingSettingsManager.getManager().updateSharingSettings(getThreadLocalRequest(), projectSharingSettings);
    }
}
