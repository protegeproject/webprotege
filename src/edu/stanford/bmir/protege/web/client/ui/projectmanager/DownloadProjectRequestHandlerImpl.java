package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.download.DownloadFormatExtensionHandler;
import edu.stanford.bmir.protege.web.client.download.DownloadSettingsDialog;
import edu.stanford.bmir.protege.web.shared.download.DownloadFormatExtension;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/04/2013
 */
public class DownloadProjectRequestHandlerImpl implements DownloadProjectRequestHandler {

    @Override
    public void handleProjectDownloadRequest(final ProjectId projectId) {
        DownloadSettingsDialog.showDialog(new DownloadFormatExtensionHandler() {
            @Override
            public void handleDownload(DownloadFormatExtension extension) {
                doDownload(projectId, extension);
            }
        });

    }

    private void doDownload(ProjectId projectId, DownloadFormatExtension extension) {
        String encodedProjectName = URL.encode(projectId.getId());
        String baseURL = GWT.getHostPageBaseURL();
        String downloadURL = baseURL + "download?ontology=" + encodedProjectName + "&format=" + extension.getExtension();
        Window.open(downloadURL, "Download ontology", "");
    }

}
