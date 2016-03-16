package edu.stanford.bmir.protege.web.client.ui.ontology.revisions;

import edu.stanford.bmir.protege.web.client.download.ProjectRevisionDownloader;
import edu.stanford.bmir.protege.web.shared.download.DownloadFormatExtension;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/04/2013
 */
public class DownloadRevisionRequestHandlerImpl implements DownloadRevisionRequestHandler {

    private ProjectId projectId;

    public DownloadRevisionRequestHandlerImpl(ProjectId projectId) {
        this.projectId = projectId;
    }

    @Override
    public void handleDownloadRevisionRequest(RevisionNumber revisionNumber) {
        ProjectRevisionDownloader downloader = new ProjectRevisionDownloader(projectId, revisionNumber, DownloadFormatExtension.owl);
        downloader.download();
    }
}
