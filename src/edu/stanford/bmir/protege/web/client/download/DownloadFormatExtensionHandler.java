package edu.stanford.bmir.protege.web.client.download;

import edu.stanford.bmir.protege.web.shared.download.DownloadFormatExtension;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2013
 */
public interface DownloadFormatExtensionHandler {

    void handleDownload(DownloadFormatExtension extension);
}
