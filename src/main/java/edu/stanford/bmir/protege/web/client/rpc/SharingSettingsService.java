package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/02/2012
 *
 * <p>
 *     A service which provides a description of permissions on a project in terms of being able to view, comment or
 *     edit the project.
 * </p>
 */
@RemoteServiceRelativePath("sharingsettings")
public interface SharingSettingsService extends RemoteService {

    ProjectSharingSettings getProjectSharingSettings(ProjectId projectId);

    void updateSharingSettings(ProjectSharingSettings projectSharingSettings);


}
