package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.download.DownloadFormatExtensionHandler;
import edu.stanford.bmir.protege.web.client.download.DownloadSettingsDialog;
import edu.stanford.bmir.protege.web.client.download.ProjectRevisionDownloader;
import edu.stanford.bmir.protege.web.client.rpc.data.RevisionNumber;
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
        RevisionNumber head = RevisionNumber.getHeadRevisionNumber();
        ProjectRevisionDownloader downloader = new ProjectRevisionDownloader(projectId, head, extension);
        downloader.download();
    }

}
