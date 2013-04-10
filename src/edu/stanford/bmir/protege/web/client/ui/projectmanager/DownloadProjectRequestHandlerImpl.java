package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/04/2013
 */
public class DownloadProjectRequestHandlerImpl implements DownloadProjectRequestHandler {

    @Override
    public void handleProjectDownloadRequest(ProjectId projectId) {
        String encodedProjectName = URL.encode(projectId.getProjectName());
        Window.open("download?ontology=" + encodedProjectName, "Download ontology", "");
    }
}
