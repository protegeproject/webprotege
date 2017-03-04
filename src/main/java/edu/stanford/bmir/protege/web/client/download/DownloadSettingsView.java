package edu.stanford.bmir.protege.web.client.download;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.shared.download.DownloadFormatExtension;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2013
 */
public interface DownloadSettingsView extends IsWidget, HasInitialFocusable {

    DownloadFormatExtension getDownloadFormatExtension();

    void setDownloadFormatExtension(DownloadFormatExtension extension);
}
